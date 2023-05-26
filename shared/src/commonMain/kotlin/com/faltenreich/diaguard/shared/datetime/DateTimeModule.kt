package com.faltenreich.diaguard.shared.datetime

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun dateTimeModule() = module {
    singleOf(::DateTimeFormatter)
}