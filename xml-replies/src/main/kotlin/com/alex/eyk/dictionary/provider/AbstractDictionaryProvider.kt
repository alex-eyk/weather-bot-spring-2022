package com.alex.eyk.dictionary.provider

import com.alex.eyk.dictionary.Dictionary
import com.alex.eyk.dictionary.Language
import com.alex.eyk.dictionary.Reply
import com.alex.eyk.dictionary.exception.NoSuchLanguageException
import com.alex.eyk.dictionary.exception.NoSuchReplyException
import com.alex.eyk.dictionary.exception.NoSuchWordException
import com.alex.eyk.xml.impl.DictionaryParser
import java.io.File

abstract class AbstractDictionaryProvider(dictionaryFiles: Set<File>) : DictionaryProvider {

    private val dictionaries: Map<String, Dictionary>
    private val languages: List<Language>

    private lateinit var defaultLanguage: Language

    init {
        val mutableDictionaries = HashMap<String, Dictionary>()
        val mutableLanguages = ArrayList<Language>()
        for (file in dictionaryFiles) {
            val dictionary = DictionaryParser().parse(file)
            if (dictionary.default) {
                if (::defaultLanguage.isInitialized) {
                    throw IllegalStateException("More than one dictionary marked as default")
                }
                this.defaultLanguage = dictionary.language
            }
            mutableLanguages.add(dictionary.language)
            mutableDictionaries[dictionary.language.code] = dictionary
        }
        this.languages = mutableLanguages
        this.dictionaries = mutableDictionaries

        if (::defaultLanguage.isInitialized == false) {
            throw IllegalStateException("No one default dictionary found")
        }
    }

    override fun getLanguages(): List<Language> {
        return languages
    }

    override fun getDefaultLanguageCode(): String {
        return defaultLanguage.code
    }

    override fun reply(lang: String, key: String): Reply {
        val dictionary = dictionaries[lang] ?: throw NoSuchLanguageException()
        return dictionary.replies[key] ?: throw NoSuchReplyException()
    }

    override fun word(lang: String, key: String): String {
        val dictionary = dictionaries[lang] ?: throw NoSuchLanguageException()
        return dictionary.words[key] ?: throw NoSuchWordException()
    }
}