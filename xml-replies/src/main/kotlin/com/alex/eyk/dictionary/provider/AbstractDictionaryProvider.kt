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
            if (file.name.endsWith(".xsd")) {
                continue
            }
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

    override fun reply(): DictionaryProvider.ReplyTransaction {
        return ReplyTransaction()
    }

    override fun word(): DictionaryProvider.WordTransaction {
        return WordTransaction()
    }

    override fun getSupportedLanguages(): List<Language> {
        return languages
    }

    override fun getDefaultLanguageCode(): String {
        return defaultLanguage.code
    }

    inner class ReplyTransaction internal constructor() : DictionaryProvider.ReplyTransaction() {

        override fun get(): Reply {
            if (args == null) {
                return reply(languageCode, key)
            }
            try {
                return reply(languageCode, key, *args!!)
            } catch (e: Exception) {
                throw IllegalStateException("Unable to parse reply with key: $key", e)
            }
        }

        private fun reply(lang: String, key: String): Reply {
            val dictionary = dictionaries[lang] ?: throw NoSuchLanguageException()
            return dictionary.replies[key] ?: throw NoSuchReplyException()
        }

        private fun reply(lang: String, key: String, vararg args: Any): Reply {
            val sourceReply = reply(lang, key)
            return Reply(
                sourceReply.content.format(*args), format = false, sourceReply.markdown
            )
        }

    }

    inner class WordTransaction internal constructor() : DictionaryProvider.WordTransaction() {

        override fun get(): String {
            return word(languageCode, key)
        }

        private fun word(lang: String?, key: String): String {
            return if (lang != null) {
                val dictionary = dictionaries[lang] ?: throw NoSuchLanguageException()
                dictionary.words[key]?.content ?: throw NoSuchWordException(
                    "Unable to find word with key: $key"
                )
            } else {
                val word = dictionaries[defaultLanguage.code]!!.words[key]
                if (word?.translatable == false) {
                    word.content
                } else {
                    throw NoSuchWordException("Unable to find word with key: $key (language was not set)")
                }
            }
        }

    }
}