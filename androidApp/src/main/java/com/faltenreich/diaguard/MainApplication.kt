package com.faltenreich.diaguard

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DependencyInjection.setup {
            // TODO: Set to none for release
            androidLogger(Level.DEBUG)
            androidContext(this@MainApplication)
        }
    }
}