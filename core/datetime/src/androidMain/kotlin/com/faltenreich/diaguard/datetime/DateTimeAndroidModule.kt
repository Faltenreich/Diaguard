package com.faltenreich.diaguard.datetime

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun dateTimePlatformModule() = module {
    factoryOf(::DateTimeAndroidApi) bind DateTimePlatformApi::class
}