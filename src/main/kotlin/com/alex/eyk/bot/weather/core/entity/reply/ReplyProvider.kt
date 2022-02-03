package com.alex.eyk.bot.weather.core.entity.reply

import com.alex.eyk.entity.Reply
import com.alex.eyk.processor.RepliesProvider

@RepliesProvider
interface ReplyProvider {

    fun of(key: String): Reply

}