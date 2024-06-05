package com.plugin.conventions.data.remote.repository.impl

import com.plugin.conventions.data.base.OperationResult
import com.plugin.conventions.data.base.SafeRequest
import com.plugin.conventions.data.remote.repository.WeatherRemoteRepository
import com.plugin.conventions.data.remote.request.WeatherRequest
import com.plugin.conventions.data.remote.response.WeatherResponse
import com.plugin.conventions.data.remote.service.WeatherService
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Response
import kotlin.math.ln

class WeatherRepositoryImp(
    private val request: SafeRequest,
    private val dispatcher: CoroutineDispatcher,
    private val service: WeatherService
) : WeatherRemoteRepository {
    override suspend fun getWeatherResponse(req: WeatherRequest): OperationResult<Response<WeatherResponse>> {
        return request.request(dispatcher) {
            service.getWeatherResponse(lat = req.lat, lng = req.lng)
        }
    }
}