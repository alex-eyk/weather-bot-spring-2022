package com.alex.eyk.bot.weather.core.entity.reply

interface ReplyProvider {

    fun getReply(key: String): Reply

}