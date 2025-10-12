package com.faltenreich.diaguard.datetime

import com.faltenreich.diaguard.datetime.format.JvmDateTimeFormatter
import com.faltenreich.diaguard.datetime.format.NativeDateTimeFormatter
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun nativeDateTimeModule() = module {
    factoryOf(::JvmDateTimeFormatter) bind NativeDateTimeFormatter::class
}