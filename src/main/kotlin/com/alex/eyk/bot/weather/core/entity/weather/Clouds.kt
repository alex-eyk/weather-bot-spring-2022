package com.alex.eyk.bot.weather.core.entity.weather

import com.google.gson.annotations.SerializedName

data class Clouds(

    @SerializedName("all")
    val all: Int

)