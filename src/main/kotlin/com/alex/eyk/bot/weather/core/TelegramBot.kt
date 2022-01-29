package com.alex.eyk.bot.weather.core

import com.alex.eyk.bot.weather.core.entity.user.UserRepository
import com.alex.eyk.bot.weather.core.handler.HandlerProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update
import java.io.Serializable
import java.util.concurrent.Executors

@Service
class TelegramBot @Autowired constructor(
    private val userRepository: UserRepository,
    private val handlerProvider: HandlerProvider,
    config: ServerConfig
) : TelegramLongPollingBot() {

    private val executorService = Executors.newFixedThreadPool(config.threads)

    @Value("\${telegram.token}")
    private lateinit var token: String

    @Value("\${telegram.bot_name}")
    private lateinit var username: String

    override fun getBotToken(): String = token

    override fun getBotUsername(): String = username

    override fun onUpdateReceived(update: Update) {
        userRepository.findByChat(update.message.chatId)
            .ifPresent { user ->
                if (user.enabled == false) {
                    return@ifPresent
                }
                val handler = handlerProvider.getHandler(user, update.message)
                val task = handler.RunnableBuilder()
                    .user(user)
                    .message(update.message)
                    .onResult {
                        execute(it)
                    }
                    .onError {

                    }
                    .build()
                executorService.submit(task)
            }
    }

    override fun <T : Serializable, Method : BotApiMethod<T>> execute(method: Method): T = super.execute(method)

}