package com.alex.eyk.bot.weather.app.net

import com.alex.eyk.bot.weather.app.entity.weather.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    @GET("weather")
    fun weather(
        @Path("lat") lat: Long, @Path("lon") lon: Long, @Path("APPID") token: String
    ): Call<WeatherResponse>

}