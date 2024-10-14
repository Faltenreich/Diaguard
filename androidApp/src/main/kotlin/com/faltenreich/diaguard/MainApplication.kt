package com.faltenreich.diaguard

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            androidLogger(if (BuildConfig.DEBUG) Level.INFO else Level.NONE)
            modules(appModules())
        }
    }
}