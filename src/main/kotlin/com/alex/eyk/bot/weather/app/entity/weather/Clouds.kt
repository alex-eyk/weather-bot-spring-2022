package com.alex.eyk.bot.weather.app.entity.weather

import com.google.gson.annotations.SerializedName

data class Clouds(

    @SerializedName("all")
    val all: Int

)