package com.alex.eyk.processor.generator

import com.squareup.kotlinpoet.TypeSpec

interface TypeGenerator<T> {

    fun generate(name: String, data: T): TypeSpec

}