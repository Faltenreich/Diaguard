package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.datetime.format.DateTimeFormatter
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun dateTimeModule() = module {
    singleOf(::DateTimeFormatter)
}