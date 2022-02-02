package com.alex.eyk.bot.weather.core.method

import org.telegram.telegrambots.meta.api.methods.send.SendMessage

fun SendMessage.SendMessageBuilder.chat(chat: Long): SendMessage.SendMessageBuilder {
    this.chatId(chat.toString())
    return this
}
