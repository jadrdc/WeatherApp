package com.plugin.conventions.weather.viewmodel.weather

import androidx.lifecycle.viewModelScope
import com.plugin.conventions.data.base.OperationResult
import com.plugin.conventions.data.model.WeatherData
import com.plugin.conventions.domain.usecase.FetchWeatherUseCase
import com.plugin.conventions.domain.usecase.GetLocationUseCase
import com.plugin.conventions.weather.base.BaseViewModel
import com.plugin.conventions.weather.viewmodel.state.WeatherState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherDataViewModel(
    private val useCase: FetchWeatherUseCase,
    private val locationUseCase: GetLocationUseCase
) :
    BaseViewModel<WeatherState, Nothing>(WeatherState(null)) {
    private val isObservingLocation = MutableStateFlow(false)

    fun startObserving(value: Boolean) {
        isObservingLocation.value = value
    }

    init {
        isObservingLocation
            .flatMapLatest { isObserving ->
                if (isObserving) {
                    locationUseCase.observeLocation(1000)
                        .flatMapLatest { pair ->
                            useCase.getWeatherData(
                                pair?.first
                                    .toString(), pair?.second.toString()
                            )
                        }
                } else {
                    flowOf(null)
                }
            }
            .onEach { result ->
                when (result) {
                    is OperationResult.Success -> {
                        setState {
                            copy(
                                data = WeatherData(
                                    url = "https://openweathermap.org/img/wn/${result.result.weather.first().icon}@2x.png",
                                    location = result.result.name,
                                    weatherCondition = result.result.weather.firstOrNull()?.main
                                        ?: "",
                                    maxTemperature = result.result.main.temp_max.toString(),
                                    minTemperature = result.result.main.temp_min.toString(),
                                    temperature = result.result.main.temp.toString() ?: ""
                                )
                            )
                        }
                    }

                    is OperationResult.Error -> {
                        // Handle error if needed
                    }

                    else -> {}
                }
            }
            .launchIn(viewModelScope)
    }
}