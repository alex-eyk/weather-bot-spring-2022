package com.alex.eyk.bot.weather.core.handler

import com.alex.eyk.bot.weather.core.entity.user.User
import com.alex.eyk.bot.weather.core.handler.command.CommandHandlerProvider
import com.alex.eyk.bot.weather.core.handler.message.MessageHandlerProvider
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import javax.inject.Inject

@Service
class HandlerProvider @Inject constructor(
    private val messageHandlerProvider: MessageHandlerProvider,
    private val commandHandlerProvider: CommandHandlerProvider
) {

    fun getHandler(user: User, message: Message): AbstractHandler<*> {
        return if (isCommand(message)) {
            val command = message.text.substring(1)
            commandHandlerProvider.byCommand(command)
        } else {
            messageHandlerProvider.byActivity(user.activity)
        }
    }

    private fun isCommand(message: Message): Boolean {
        return message.text != null && message.text.startsWith("/")
    }

}