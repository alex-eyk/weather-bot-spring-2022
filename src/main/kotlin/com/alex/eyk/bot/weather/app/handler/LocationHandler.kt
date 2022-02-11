package com.alex.eyk.bot.weather.app.handler

import com.alex.eyk.bot.weather.app.entity.weather.WeatherResponse
import com.alex.eyk.bot.weather.app.net.WeatherService
import com.alex.eyk.bot.weather.core.entity.reply.impl.Replies
import com.alex.eyk.bot.weather.core.entity.user.User
import com.alex.eyk.bot.weather.core.handler.message.condition.ConditionMessageHandler
import com.alex.eyk.dictionary.provider.DictionaryProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class LocationHandler(
    private val weatherService: WeatherService,
    private val dictProvider: DictionaryProvider,
) : ConditionMessageHandler(condition) {

    companion object {
        val condition: (User, Message) -> Boolean = { _, message ->
            message.location != null
        }
    }

    @Value("\${weather.token}")
    private lateinit var token: String

    override fun saveHandle(user: User, message: Message): SendMessage {
        val location = message.location
        val request = weatherService
            .weather(location.latitude, location.longitude, token)
            .execute()
        if (request.isSuccessful) {
            return sendWeather(user, request.body()!!)
        }
        return sendErrorReply(user)
    }

    private fun sendErrorReply(user: User): SendMessage {
        val errorReply = dictProvider.reply()
            .language(user.languageCode)
            .key(Replies.UNABLE_TO_GET_WEATHER)
            .get()
        return super.sendSimpleReply(user, errorReply)
    }

    private fun sendWeather(user: User, weather: WeatherResponse): SendMessage {
        val weatherReply = dictProvider.reply()
            .language(user.languageCode)
            .key(Replies.WEATHER)
            .args(weather.temperature.real)
            .get()
        return super.sendSimpleReply(user, weatherReply)
    }
}