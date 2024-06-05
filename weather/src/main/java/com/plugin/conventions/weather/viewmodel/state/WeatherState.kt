package com.plugin.conventions.weather.viewmodel.state

import com.plugin.conventions.data.model.WeatherData
import com.plugin.conventions.weather.base.ViewModelState

data class WeatherState(val data: WeatherData?) : ViewModelState
