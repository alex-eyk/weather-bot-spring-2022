package com.alex.eyk.processor.generate

import com.squareup.kotlinpoet.FileSpec

interface FileGenerator<T> {

    fun generate(name: String, data: T): FileSpec

}