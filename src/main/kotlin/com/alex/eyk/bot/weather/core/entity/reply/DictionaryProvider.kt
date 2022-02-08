package com.alex.eyk.bot.weather.core.entity.reply

import com.alex.eyk.entity.Reply
import com.alex.eyk.processor.DictionaryProvider

@DictionaryProvider
interface DictionaryProvider {

    @Deprecated("Use of(lang, key) instead")
    fun of(key: String): Reply

    fun word(lang: String, key: String): String

    fun reply(lang: String, key: String): Reply

}