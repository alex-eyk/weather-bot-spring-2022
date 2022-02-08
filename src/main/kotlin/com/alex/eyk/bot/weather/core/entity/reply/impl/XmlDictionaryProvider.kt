package com.alex.eyk.bot.weather.core.entity.reply.impl

import com.alex.eyk.bot.weather.core.entity.reply.DictionaryProvider
import com.alex.eyk.bot.weather.core.util.ResourceUtils
import com.alex.eyk.entity.Dictionary
import com.alex.eyk.entity.Reply
import com.alex.eyk.xml.impl.DictionaryParser
import org.springframework.stereotype.Service

const val DICTIONARY_DIRECTORY = "dictionary/"

@Service
class XmlDictionaryProvider : DictionaryProvider {

    @Deprecated("use dictionary")
    private val replies: Map<String, Reply> = HashMap()

    private val dictionaries: Map<String, Dictionary>

    init {
        val mutableDictionaries = HashMap<String, Dictionary>()
        val dictionaryFiles = ResourceUtils.scanResourceDirectory(DICTIONARY_DIRECTORY)
        for (file in dictionaryFiles) {
            val dictionary = DictionaryParser().parse(file)
            mutableDictionaries[dictionary.code] = dictionary
        }
        this.dictionaries = mutableDictionaries
    }

    override fun of(key: String): Reply {
        return replies[key] ?: throw IllegalArgumentException("No reply for key: $key")
    }

    override fun reply(lang: String, key: String): Reply {
        return dictionaries[lang]?.replies?.get(key) ?: throw IllegalStateException("")
    }

    override fun word(lang: String, key: String): String {
        return dictionaries[lang]?.words?.get(key) ?: throw IllegalStateException("")
    }


}