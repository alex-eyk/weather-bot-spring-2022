package com.alex.eyk.processor

import com.alex.eyk.util.CaseUtils
import com.alex.eyk.util.removeLastChars
import com.squareup.kotlinpoet.*
import java.io.File
import kotlin.reflect.KClass

class ArgsBuilder {

    companion object {

        private const val BUILDER_PACKAGE = "com.alex.eyk.dictionary.builder"

        private val argToTypeMap = mapOf(
            Pair('s', String::class),
            Pair('d', Int::class),
            Pair('f', Float::class)
        )
    }

    private val consideredArgs: MutableSet<String> = HashSet()

    fun generateArgBuilders(
        repliesArgs: Map<String, List<Argument>>, generatedFilesDir: String
    ) {
        for (argsEntry in repliesArgs) {
            val type = createTypeSpec(argsEntry.key, argsEntry.value)
            val fileSpec = createFileSpec(type)
            fileSpec.writeTo(File(generatedFilesDir))
        }
    }

    private fun createTypeSpec(
        replyKey: String, args: List<Argument>
    ): TypeSpec {
        val classBuilder = TypeSpec.classBuilder(replyKey.className())
        for (arg in args) {
            if (consideredArgs.contains(arg.name)) {
                continue
            }
            classBuilder
                .addProperty(makePropertySpec(arg))
                .addFunction(makeSetFunSpec(arg))
            consideredArgs.add(arg.name)

        }
        return classBuilder
            .addFunction(makeBuildFunction(args))
            .build()
    }

    private fun makePropertySpec(arg: Argument): PropertySpec {
        val argumentType = getArgumentType(arg)
        return PropertySpec
            .builder(arg.propertyName(), argumentType)
            .delegate(CodeBlock.of("Delegates.notNull<%T>()", argumentType))
            .addModifiers(KModifier.PRIVATE)
            .build()
    }

    private fun makeSetFunSpec(arg: Argument): FunSpec {
        return FunSpec.builder(arg.propertyName())
            .addParameter("value", getArgumentType(arg))
            .addStatement("apply { this.%L = value }", arg.propertyName())
            .build()

    }

    private fun makeBuildFunction(args: List<Argument>): FunSpec {
        return FunSpec.builder("build")
            .returns(Array<out Any>::class)
            .addStatement("return arrayOf(%L)", makeArgsLine(args))
            .build()
    }

    private fun createFileSpec(typeSpec: TypeSpec): FileSpec {
        return FileSpec
            .builder(BUILDER_PACKAGE, typeSpec.name!!)
            .addType(typeSpec)
            .build()
    }

    private fun getArgumentType(arg: Argument): KClass<*> {
        for (c in arg.query.toCharArray()) {
            if (c.isLetter()) {
                return argToTypeMap[c] ?: throw IllegalStateException()
            }
        }
        throw IllegalStateException("Unable to get type for query: ${arg.query}")
    }

    private fun makeArgsLine(args: List<Argument>): String {
        val lineBuilder = StringBuilder()
        for (arg in args) {
            val name = CaseUtils
                .convertSnakeCaseToPropertyName(arg.name)
            lineBuilder
                .append(name)
                .append(", ")
        }
        return lineBuilder.removeLastChars(2)
            .toString()
    }

    private fun Argument.propertyName(): String {
        return CaseUtils.convertSnakeCaseToPropertyName(this.name)
    }

    private fun String.className(): String {
        return CaseUtils.convertSnakeCaseToClassName(this)
    }
}
