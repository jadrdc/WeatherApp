package com.plugin.conventions.weather.app

import android.app.Application
import com.plugin.conventions.weather.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                viewModelModule
            )
        }
    }
}