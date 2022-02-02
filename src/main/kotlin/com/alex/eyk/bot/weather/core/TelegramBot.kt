package com.alex.eyk.bot.weather.core

import com.alex.eyk.bot.weather.core.config.ServerProperties
import com.alex.eyk.bot.weather.core.entity.user.UserRepository
import com.alex.eyk.bot.weather.core.handler.HandlerProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import org.telegram.telegrambots.meta.updateshandlers.SentCallback
import java.io.Serializable
import java.util.concurrent.Executors

@Service
class TelegramBot @Autowired constructor(
    private val userRepository: UserRepository,
    private val handlerProvider: HandlerProvider,
    config: ServerProperties
) : TelegramLongPollingBot() {

    private val logger = LoggerFactory.getLogger(TelegramBot::class.java)

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
                val task = handler.TaskBuilder()
                    .user(user)
                    .message(update.message)
                    .onResult {
                        executeAsync(it, object : ExceptionCallback {
                            override fun onApiException(method: BotApiMethod<*>, e: TelegramApiRequestException) {
                                logger.error(
                                    "API Exception during execute async api method: $method, " +
                                            "message: ${update.message}",
                                    e
                                )
                            }

                            override fun onException(method: BotApiMethod<*>, e: Exception) {
                                logger.error("Exception during execute async api method", e)
                            }
                        })
                    }
                    .onError {

                    }
                    .build()
                executorService.submit(task)
            }
    }

    override fun <T : Serializable, Method : BotApiMethod<T>> execute(method: Method): T = super.execute(method)

    fun <T : Serializable, Method : BotApiMethod<T>> executeAsync(
        method: Method,
        callback: ExceptionCallback
    ) {
        super.executeAsync(method, object : SentCallback<T> {

            override fun onResult(method: BotApiMethod<T>, response: T) {
            }

            override fun onError(method: BotApiMethod<T>, apiException: TelegramApiRequestException) {
                callback.onApiException(method, apiException)
            }

            override fun onException(method: BotApiMethod<T>, exception: java.lang.Exception) {
                callback.onException(method, exception)
            }
        })
    }
}

interface ExceptionCallback {

    fun onApiException(method: BotApiMethod<*>, e: TelegramApiRequestException)

    fun onException(method: BotApiMethod<*>, e: Exception)

}