package com.faltenreich.diaguard.logging

import com.faltenreich.diaguard.config.BuildConfig
import org.koin.dsl.module

fun loggingModule() = module {
    single<Logger> {
        if (get<BuildConfig>().hasPlatformFramework()) PlatformLogger()
        else ConsoleLogger()
    }
}