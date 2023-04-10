package com.faltenreich.rhyme

import android.app.Application
import com.faltenreich.rhyme.shared.di.DependencyInjection
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DependencyInjection.setup {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@MainApplication)
        }
    }
}