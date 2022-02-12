package com.alex.eyk.bot.weather.app.handler

import com.alex.eyk.bot.weather.app.net.WeatherService
import com.alex.eyk.bot.weather.core.entity.user.User
import com.alex.eyk.bot.weather.core.entity.weather.*
import com.alex.eyk.bot.weather.core.handler.message.condition.ConditionMessageHandler
import com.alex.eyk.dictionary.builder.WeatherArgumentsBuilder
import com.alex.eyk.dictionary.keys.Replies
import com.alex.eyk.dictionary.keys.Words
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
            .weather(
                location.latitude,
                location.longitude,
                token,
                user.units.unitName(),
                user.languageCode
            )
            .execute()
        if (request.isSuccessful) {
            return sendWeather(user, request.body())
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
        val args = WeatherArgumentsBuilder()
            .realTemp(weather.temperature.real)
            .tempSign(getDegreeSign(user))
            .feelsTemp(weather.temperature.feelsLike)
            .description(getWeatherDescription(weather.weatherList))
            .direction(getWindDirection(user, weather.wind))
            .speed(weather.wind.speed)
            .speedDimension(getSpeedDimension(user))
            .build()
        val weatherReply = dictProvider.reply()
            .language(user.languageCode)
            .key(Replies.WEATHER)
            .args(*args)
            .get()
        return super.sendSimpleReply(user, weatherReply)
    }

    private fun getDegreeSign(user: User): String {
        val key = when (user.units) {
            Units.STANDARD -> Words.KELVIN
            Units.METRIC -> Words.CELSIUS
            Units.IMPERIAL -> Words.FAHRENHEIT
        }
        return dictProvider.word()
            .key(key)
            .get()
    }

    private fun getWindDirection(user: User, wind: Wind): String {
        val key = WindDirection.ofDegrees(wind.degrees).directionName()
        return dictProvider.word()
            .language(user.languageCode)
            .key(key)
            .get()
    }

    private fun getSpeedDimension(user: User): String {
        val key =
            if (user.units == Units.METRIC || user.units == Units.STANDARD) {
                Words.METERS_PER_SECOND
            } else {
                Words.MILES_PER_SECOND
            }
        return dictProvider.word()
            .language(user.languageCode)
            .key(key)
            .get()
    }

    private fun getWeatherDescription(weatherList: List<Weather>): String {
        val descriptionBuilder = StringBuilder()
        for (weather in weatherList) {
            descriptionBuilder.append(weather.description)
                .append(", ")
        }
        val length = descriptionBuilder.length
        return descriptionBuilder.delete(length - 2, length)
            .toString()
    }

}