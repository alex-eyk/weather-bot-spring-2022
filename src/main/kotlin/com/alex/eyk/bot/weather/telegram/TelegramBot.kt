package com.alex.eyk.bot.weather.telegram

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.concurrent.Executors

@Service
class TelegramBot @Autowired constructor(
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
        TODO("Not yet implemented")
    }

}