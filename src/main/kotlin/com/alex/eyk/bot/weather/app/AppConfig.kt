package com.alex.eyk.bot.weather.app

import com.alex.eyk.bot.weather.core.ServerConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {

    @Bean
    fun serverConfig(): ServerConfig = ServerConfig()

}