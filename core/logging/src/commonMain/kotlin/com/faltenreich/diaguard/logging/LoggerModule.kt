package com.faltenreich.diaguard.logging

import org.koin.dsl.module

fun loggerModule() = module {
    single<Logger> { PlatformLogger() }
}