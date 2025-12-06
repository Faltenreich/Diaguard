package com.faltenreich.diaguard.datetime

import com.faltenreich.diaguard.localization.FakeLocalization
import com.faltenreich.diaguard.localization.Localization
import org.koin.dsl.bind
import org.koin.dsl.module

internal fun testModule() = module {
    factory { FakeLocalization() } bind Localization::class
    factory<DateTimePlatformApi> { DateTimeFakeApi(is24HourFormat = true) }
}