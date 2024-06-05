package com.plugin.conventions.data.model

data class WeatherData(
    val location: String,
    val minTemperature: String,
    val maxTemperature: String,
    val temperature: String,
    val weatherCondition: String,
    val url: String = ""
)
