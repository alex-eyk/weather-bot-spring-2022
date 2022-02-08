package com.alex.eyk.bot.weather.app.handler

import com.alex.eyk.bot.weather.core.entity.reply.DictionaryProvider
import com.alex.eyk.bot.weather.core.entity.user.User
import com.alex.eyk.bot.weather.core.handler.command.CommandHandler
import com.alex.eyk.bot.weather.core.method.SendMessageBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

private const val COMMAND = "start"

@Service
class StartHandler @Autowired constructor(
    private val dictProvider: DictionaryProvider
) : CommandHandler(COMMAND) {

    override fun saveHandle(user: User, message: Message): SendMessage {
        return SendMessageBuilder()
            .chat(user.chat)
            //.reply(dictProvider.reply(Replies.START))
            .build()
    }

}
