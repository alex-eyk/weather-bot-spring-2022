package com.alex.eyk.bot.weather.app.handler

import com.alex.eyk.bot.weather.core.entity.weather.WeatherResponse
import com.alex.eyk.bot.weather.app.net.WeatherService
import com.alex.eyk.bot.weather.core.dictionary.Replies
import com.alex.eyk.bot.weather.core.dictionary.Words
import com.alex.eyk.bot.weather.core.entity.user.User
import com.alex.eyk.bot.weather.core.entity.weather.Units
import com.alex.eyk.bot.weather.core.entity.weather.Wind
import com.alex.eyk.bot.weather.core.entity.weather.WindDirection
import com.alex.eyk.bot.weather.core.handler.message.condition.ConditionMessageHandler
import com.alex.eyk.dictionary.provider.DictionaryProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.properties.Delegates

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
            .weather(location.latitude, location.longitude, token, user.units.unitName())
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
        val args = WeatherArgsBuilder()
            .realTemperature(weather.temperature.real)
            .degreeSign(getDegreeSign(user))
            .windDirection(getWindDirection(user, weather.wind))
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

    class WeatherArgsBuilder {

        private var realTemperature by Delegates.notNull<Float>()
        private lateinit var degreeSign: String
        private lateinit var windDirection: String

        fun realTemperature(temp: Float) = apply { this.realTemperature = temp }

        fun degreeSign(sign: String) = apply { this.degreeSign = sign }

        fun windDirection(direction: String) = apply { this.windDirection = direction }

        fun build(): Array<out Any> {
            return arrayOf(realTemperature, degreeSign, windDirection)
        }
    }
}