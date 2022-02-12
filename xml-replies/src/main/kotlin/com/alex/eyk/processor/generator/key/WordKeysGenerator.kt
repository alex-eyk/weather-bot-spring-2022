package com.alex.eyk.processor.generator.key

import com.alex.eyk.dictionary.Dictionary
import com.squareup.kotlinpoet.TypeSpec

class WordKeysGenerator : AbstractDataKeysGenerator() {

    override fun generate(name: String, data: Dictionary): TypeSpec {
        return super.generateObjectForKeys(name, data.words.keys)
    }

}