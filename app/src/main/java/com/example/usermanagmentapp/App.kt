package com.example.usermanagmentapp

import android.app.Application
import com.example.usermanagmentapp.sl.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            // Reference Android context
            androidContext(this@App)
            // Load modules
            modules(appModule)
        }
    }
}