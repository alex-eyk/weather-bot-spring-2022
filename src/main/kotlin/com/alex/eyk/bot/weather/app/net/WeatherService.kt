package com.alex.eyk.bot.weather.app.net

import com.alex.eyk.bot.weather.core.entity.weather.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    fun weather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("APPID") token: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): Call<WeatherResponse>

}