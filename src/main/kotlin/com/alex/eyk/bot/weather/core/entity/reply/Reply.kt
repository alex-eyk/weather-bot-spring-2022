package com.alex.eyk.bot.weather.core.entity.reply

import com.alex.eyk.bot.processor.ReplyEntity

@ReplyEntity
data class Reply(
    val content: String,
    val format: Boolean,
    val markdown: Boolean
)