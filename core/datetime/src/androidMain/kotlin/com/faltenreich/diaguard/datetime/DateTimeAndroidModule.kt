package com.faltenreich.diaguard.datetime

import com.faltenreich.diaguard.config.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun dateTimePlatformModule() = module {
    factory<DateTimePlatformApi> {
        if (get<BuildConfig>().hasPlatformFramework()) DateTimeAndroidApi(get(), androidContext())
        else DateTimePlatformFakeApi()
    }
}