package com.alex.eyk.bot.weather.core.entity.reply

interface ReplyProvider {

    fun of(key: String): Reply

}