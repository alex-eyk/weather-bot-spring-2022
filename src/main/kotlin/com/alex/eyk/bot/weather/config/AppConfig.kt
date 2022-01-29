package com.alex.eyk.bot.weather.config

import com.alex.eyk.bot.weather.telegram.ServerConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {

    @Bean
    fun serverConfig(): ServerConfig = ServerConfig()

}