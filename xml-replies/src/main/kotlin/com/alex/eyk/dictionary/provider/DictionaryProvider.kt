package com.alex.eyk.dictionary.provider

import com.alex.eyk.dictionary.Reply

interface DictionaryProvider {

    fun word(lang: String, key: String): String

    fun reply(lang: String, key: String): Reply

    fun getDefaultLanguageCode(): String

}