package com.faltenreich.diaguard.shared.logging

import org.koin.dsl.module

fun loggerModule() = module {
    single<Logger> { PlatformLogger() }
}