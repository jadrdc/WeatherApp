package com.plugin.conventions.data.remote.service

import com.plugin.conventions.data.base.OperationResult
import com.plugin.conventions.data.remote.response.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {

    @GET("weather")
    suspend fun getWeatherResponse(
        @Query("lat") lat: String,
        @Query("lon") lng: String,
        @Query("APPID") apiKey: String = "dcfdda6e1cff5c7803c4062ab0bb10b9",
        @Query("units") unit: String = "metric"
    ): Response<WeatherResponse>
}

//http://api.openweathermap.org/data/2.5/weather?lat=39.099724&lon=-94.578331&APPID=dcfdda6e1cff5c7803c4062ab0bb10b9&units=metric