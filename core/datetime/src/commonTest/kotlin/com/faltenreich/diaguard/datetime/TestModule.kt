package com.faltenreich.diaguard.datetime

import org.koin.dsl.module

internal fun testModule() = module {
    factory<DateTimePlatformApi> { DateTimeFakeApi(is24HourFormat = true) }
}