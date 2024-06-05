package com.plugin.conventions.weather.di

import com.plugin.conventions.data.base.SafeRequest
import com.plugin.conventions.data.remote.repository.WeatherRemoteRepository
import com.plugin.conventions.data.remote.repository.impl.WeatherRepositoryImp
import com.plugin.conventions.data.remote.service.WeatherService
import com.plugin.conventions.domain.usecase.FetchWeatherUseCase
import com.plugin.conventions.domain.usecase.GetLocationUseCase
import com.plugin.conventions.weather.viewmodel.weather.WeatherDataViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECT_TIMEOUT = 10L
private const val WRITE_TIMEOUT = 1L
private const val READ_TIMEOUT = 20L

val viewModelModule = module {
    single {
        OkHttpClient.Builder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }.build()
    }
    single {
        Retrofit.Builder().client(get()).baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
    single<WeatherService> { get<Retrofit>().create(WeatherService::class.java) }
    single<WeatherRemoteRepository> { WeatherRepositoryImp(SafeRequest(), Dispatchers.IO, get()) }
    single { FetchWeatherUseCase(get()) }
    single { GetLocationUseCase(androidContext()) }
    single { WeatherDataViewModel(get(), get()) }
}