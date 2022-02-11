package com.alex.eyk.bot.weather.core.entity.weather

enum class Units {

    STANDARD,
    METRIC,
    IMPERIAL;

    fun unitName(): String {
        return this.name.lowercase()
    }

}