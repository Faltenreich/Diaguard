package com.faltenreich.diaguard

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class TestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(appModules())
        }
    }

    override fun onTerminate() {
        stopKoin()
        super.onTerminate()
    }
}