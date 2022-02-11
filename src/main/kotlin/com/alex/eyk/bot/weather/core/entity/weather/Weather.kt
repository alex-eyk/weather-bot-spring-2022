package com.alex.eyk.bot.weather.core.entity.weather

import com.google.gson.annotations.SerializedName

data class Weather(

    @SerializedName("main")
    val name: String,

    @SerializedName("description")
    val description: String

)
