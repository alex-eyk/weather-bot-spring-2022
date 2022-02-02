package com.alex.eyk.bot.weather.core.config

import com.ximand.properties.PropertiesProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoreConfig {

    @Bean
    fun serverProperties(): ServerProperties {
        val propertiesProvider = PropertiesProvider()
        return propertiesProvider.createInstance(ServerProperties::class.java)
    }

}