package com.alex.eyk.bot.weather.app.entity.weather

import com.google.gson.annotations.SerializedName

data class Temperature(

    @SerializedName("temp")
    val real: Float,

    @SerializedName("feels_like")
    val feelsLike: Float,

    @SerializedName("humidity")
    val humidity: Float

)