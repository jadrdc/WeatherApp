package com.plugin.conventions.domain.usecase

import com.plugin.conventions.data.base.OperationResult
import com.plugin.conventions.data.model.WeatherData
import com.plugin.conventions.data.remote.repository.WeatherRemoteRepository
import com.plugin.conventions.data.remote.request.WeatherRequest
import com.plugin.conventions.data.remote.response.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchWeatherUseCase(
    private val repository: WeatherRemoteRepository
) {

    fun getWeatherData(lat: String, lng: String): Flow<OperationResult<WeatherResponse>> {
        return flow {
            when (val result =
                repository.getWeatherResponse(WeatherRequest(lat = lat, lng = lng))) {
                is OperationResult.Success -> {
                    val data =
                        result.result.body()
                    if (data != null)
                        emit(
                            OperationResult.Success(
                                data.copy()
                            )
                        )
                }

                is OperationResult.Error -> {
                    emit(result)
                }
            }
        }
    }
}