@file:Suppress("unused")

package com.alex.eyk.bot.processor

import com.alex.eyk.bot.processor.xml.impl.KeysParser
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.ximand.properties.PropertiesProvider
import java.io.File
import java.io.FileInputStream
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
        return mutableSetOf(ReplyEntity::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        FileInputStream(properties.repliesAbsolutePath).use {
            val keys = KeysParser().parse(it)
            val keysObjectSpec = generateObjectForKeys(keys)
            val annotatedElements = roundEnv.getElementsAnnotatedWith(ReplyEntity::class.java)
            if (annotatedElements.isEmpty()) {
                return false
            } else if (annotatedElements.size > 1) {
                throw IllegalStateException("Only one class can be annotated with @ReplyEntity.")
            }
            val replyPackage = getReplyPackage(annotatedElements.getSingle())
            val fileSpec = createRepliesFileSpec(replyPackage, keysObjectSpec)
            val generatedFilesDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
            fileSpec.writeTo(File(generatedFilesDir!!))
        }
        return true
    }

    private fun generateObjectForKeys(keys: Set<String>): TypeSpec {
        val builder = TypeSpec.objectBuilder(properties.generatedFileName)
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
            .builder(replyPackage, properties.generatedFileName)
            .addType(keysObjectSpec)
            .build()
    }

    private fun getReplyPackage(replyElement: Element): String {
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

fun main() {
    FileInputStream("/Users/aleksejkiselev/IdeaProjects/weather-bot-spring-2022/src/main/resources/replies.xml").use {
        val keys = KeysParser().parse(it)
        print(keys)
    }
}