package com.alex.eyk.bot.weather.core.entity.weather

import com.google.gson.annotations.SerializedName

data class Wind(

    @SerializedName("speed")
    val speed: Float,

    @SerializedName("deg")
    val degrees: Float

)
