package com.alex.eyk.bot.weather.app.entity.weather

import com.google.gson.annotations.SerializedName

data class WeatherResponse(

    @SerializedName("weather")
    val weatherList: List<Weather>,

    @SerializedName("main")
    val temperature: Temperature,

    @SerializedName("wind")
    val wind: Wind,

    @SerializedName("clouds")
    val clouds: Clouds

)