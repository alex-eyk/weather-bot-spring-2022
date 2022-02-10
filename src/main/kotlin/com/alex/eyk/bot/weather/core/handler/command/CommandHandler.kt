package com.alex.eyk.bot.weather.core.handler.command

import com.alex.eyk.bot.weather.core.entity.user.User
import com.alex.eyk.bot.weather.core.handler.AbstractHandler
import com.alex.eyk.bot.weather.core.method.SendMessageBuilder
import com.alex.eyk.dictionary.Reply
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

abstract class CommandHandler(
    val command: String
) : AbstractHandler() {

    protected fun sendSimpleReply(user: User, reply: Reply): SendMessage {
        return SendMessageBuilder()
            .chat(user.chat)
            .reply(reply)
            .build()
    }

}