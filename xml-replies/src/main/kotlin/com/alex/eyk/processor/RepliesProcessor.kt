@file:Suppress("unused")

package com.alex.eyk.processor

import com.alex.eyk.dictionary.Dictionary
import com.alex.eyk.xml.impl.DictionaryParser
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.ximand.properties.PropertiesProvider
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

class RepliesProcessor : AbstractProcessor() {

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    private val properties: ProcessorProperties = PropertiesProvider()
        .createInstance(ProcessorProperties::class.java)

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(DictionaryProvider::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        val dictionaryFiles = getAllDictionaryFiles()
        val defaultDictionary = getDefaultDictionary(dictionaryFiles)
        val specs = generateSpecs(defaultDictionary)
        val annotatedElements = roundEnv.getElementsAnnotatedWith(DictionaryProvider::class.java)
        if (annotatedElements.isEmpty()) {
            return false
        } else if (annotatedElements.size > 1) {
            throw IllegalStateException("Only one class can be annotated with @DictionaryProvider.")
        }
        val replyPackage = getDictionaryPackage(annotatedElements.getSingle())
        for (spec in specs) {
            val fileSpec = createRepliesFileSpec(replyPackage, spec)
            val generatedFilesDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
            fileSpec.writeTo(File(generatedFilesDir!!))
        }
        return true
    }

    private fun getAllDictionaryFiles(): Array<File> {
        val directory = File(properties.dictionariesDirectoryPath)
        return directory.listFiles()
            ?: throw IllegalStateException(
                "No one dictionary file found, path: ${properties.dictionariesDirectoryPath}"
            )
    }

    private fun getDefaultDictionary(dictionaryFiles: Array<File>): Dictionary {
        var defaultDictionary: Dictionary? = null
        for (file in dictionaryFiles) {
            if (file.name.endsWith(".xsd") == false) {
                val dictionary = DictionaryParser().parse(file)
                if (dictionary.default) {
                    defaultDictionary = dictionary
                }
            }
        }
        return defaultDictionary ?: throw IllegalStateException("Unable to found default dictionary")
    }

    private fun generateSpecs(dictionary: Dictionary): Set<TypeSpec> {
        return setOf(
            generateObjectForKeys(properties.generatedReplyFilename, dictionary.replies.keys),
            generateObjectForKeys(properties.generatedWordFilename, dictionary.words.keys)
        )
    }

    private fun generateObjectForKeys(name: String, keys: Set<String>): TypeSpec {
        val builder = TypeSpec.objectBuilder(name)
        for (key in keys) {
            builder.addProperty(
                PropertySpec.builder(key.uppercase(), String::class)
                    .mutable(false)
                    .initializer("%S", key)
                    .build()
            )
        }
        return builder.build()
    }

    private fun createRepliesFileSpec(replyPackage: String, keysObjectSpec: TypeSpec): FileSpec {
        return FileSpec
            .builder(replyPackage, keysObjectSpec.name!!)
            .addType(keysObjectSpec)
            .build()
    }

    private fun getDictionaryPackage(replyElement: Element): String {
        val annotatedElement = processingEnv
            .typeUtils
            .asElement(replyElement.asType())
        return processingEnv
            .elementUtils
            .getPackageOf(annotatedElement)
            .toString()
    }

}

private fun <T> Set<T>.getSingle(): T {
    this.forEach {
        return it
    }
    throw IllegalStateException("Unable to get single object because set is empty")
}