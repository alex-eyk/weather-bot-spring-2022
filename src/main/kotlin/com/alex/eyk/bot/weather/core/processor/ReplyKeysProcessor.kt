package com.alex.eyk.bot.weather.core.processor

import com.alex.eyk.bot.weather.core.entity.reply.impl.REPLIES_PATH
import com.alex.eyk.bot.weather.core.xml.impl.ReplyXmlParser
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import java.io.IOException
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
const val REPLIES_OBJECT_NAME = "Replies"

@Suppress("unused")
@AutoService(Processor::class)
class ReplyKeysProcessor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(ReplyEntity::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        val replyKeys = readKeys()
        val keysObjectSpec = generateObjectSpec(replyKeys)
        val annotatedElements = roundEnv.getElementsAnnotatedWith(ReplyEntity::class.java)
        if (annotatedElements.isEmpty()) {
            return false
        } else if (annotatedElements.size > 1) {
            throw IllegalStateException("Only one class can be annotated with @ReplyEntity.")
        }
        val replyPackage = getReplyPackage(annotatedElements.getSingle())
        val file = createRepliesFileSpec(replyPackage, keysObjectSpec)
        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
        file.writeTo(File(kaptKotlinGeneratedDir))
        return true
    }

    private fun readKeys(): Set<String> {
        try {
            javaClass.classLoader.getResourceAsStream(REPLIES_PATH)
                .use { inputStream ->
                    if (inputStream != null) {
                        return ReplyXmlParser()
                            .parse(inputStream)
                            .keys
                    } else {
                        throw IllegalStateException("input stream == null")
                    }
                }
        } catch (e: IOException) {
            throw IllegalStateException(
                "Unable to close replies.xml input stream", e
            )
        }
    }

    private fun generateObjectSpec(keys: Set<String>): TypeSpec {
        val builder = TypeSpec.objectBuilder(REPLIES_OBJECT_NAME)
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

    private fun getReplyPackage(replyElement: Element): String {
        val annotatedElement = processingEnv
            .typeUtils
            .asElement(replyElement.asType())
        return processingEnv
            .elementUtils
            .getPackageOf(annotatedElement)
            .toString()
    }

    private fun createRepliesFileSpec(replyPackage: String, keysObjectSpec: TypeSpec): FileSpec {
        return FileSpec
            .builder(replyPackage, REPLIES_OBJECT_NAME)
            .addType(keysObjectSpec)
            .build()
    }

}

private fun <T> Set<T>.getSingle(): T {
    this.forEach {
        return it
    }
    throw IllegalStateException("Unable to get single object because set is empty")
}
