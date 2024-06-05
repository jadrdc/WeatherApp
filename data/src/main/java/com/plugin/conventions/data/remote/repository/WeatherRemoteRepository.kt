package com.plugin.conventions.data.remote.repository

import com.plugin.conventions.data.base.OperationResult
import com.plugin.conventions.data.remote.request.WeatherRequest
import com.plugin.conventions.data.remote.response.WeatherResponse
import retrofit2.Response

interface WeatherRemoteRepository {

    suspend fun getWeatherResponse(req: WeatherRequest): OperationResult<Response<WeatherResponse>>
}