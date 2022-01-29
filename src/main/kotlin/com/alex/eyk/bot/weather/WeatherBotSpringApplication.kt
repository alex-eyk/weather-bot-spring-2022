package com.alex.eyk.bot.weather

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WeatherBotSpringApplication

fun main(args: Array<String>) {
    runApplication<WeatherBotSpringApplication>(*args)
}
