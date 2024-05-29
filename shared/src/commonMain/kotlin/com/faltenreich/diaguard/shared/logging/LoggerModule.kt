package com.faltenreich.diaguard.shared.logging

import org.koin.core.module.Module
import org.koin.dsl.module

fun loggerModule(): Module = module {
    single<Logger> { PlatformLogger() }
}