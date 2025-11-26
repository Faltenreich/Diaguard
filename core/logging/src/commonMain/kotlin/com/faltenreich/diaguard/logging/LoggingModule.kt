package com.faltenreich.diaguard.logging

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun loggingModule() = module {
    singleOf(::PlatformLogger) bind Logger::class
}