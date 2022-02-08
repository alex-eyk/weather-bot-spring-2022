package com.alex.eyk.dictionary.provider

import com.alex.eyk.dictionary.Dictionary
import com.alex.eyk.dictionary.Reply
import com.alex.eyk.dictionary.exception.NoSuchLanguageException
import com.alex.eyk.dictionary.exception.NoSuchReplyException
import com.alex.eyk.dictionary.exception.NoSuchWordException
import com.alex.eyk.xml.impl.DictionaryParser
import java.io.File

abstract class AbstractDictionaryProvider(dictionaryFiles: Set<File>) : DictionaryProvider {

    private val dictionaries: Map<String, Dictionary>
    private var defaultCode: String? = null

    init {
        val mutableDictionaries = HashMap<String, Dictionary>()
        for (file in dictionaryFiles) {
            val dictionary = DictionaryParser().parse(file)
            if (dictionary.default) {
                if (defaultCode != null) {
                    throw IllegalStateException("More than one dictionary marked as default")
                }
                this.defaultCode = dictionary.code
            }
            mutableDictionaries[dictionary.code] = dictionary
        }
        this.dictionaries = mutableDictionaries
        if (defaultCode == null) {
            throw IllegalStateException("No one default dictionary found")
        }
    }

    override fun reply(lang: String, key: String): Reply {
        val dictionary = dictionaries[lang] ?: throw NoSuchLanguageException()
        return dictionary.replies[key] ?: throw NoSuchReplyException()
    }

    override fun word(lang: String, key: String): String {
        val dictionary = dictionaries[lang] ?: throw NoSuchLanguageException()
        return dictionary.words[key] ?: throw NoSuchWordException()
    }

    override fun getDefaultLanguageCode(): String {
        return defaultCode!!
    }
}