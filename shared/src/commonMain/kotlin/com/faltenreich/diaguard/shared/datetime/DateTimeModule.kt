package com.faltenreich.diaguard.shared.datetime

import org.koin.dsl.module

fun dateTimeModule() = module {
    single<DateTimeApi> { KotlinxDateTimeApi(KotlinxDateTimeMapper()) }
}