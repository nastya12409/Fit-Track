package com.example.fitnesstracker

import android.app.Application
import com.example.fitnesstracker.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

// Клас застосунку: тут стартує Koin і бачить усі модулі DI
class FitTrackApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FitTrackApp) // Context для Room та інших модулів
            modules(appModules)               // усі модулі разом
        }
    }
}