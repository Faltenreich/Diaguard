package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.datetime.kotlinx.KotlinxDateTimeApi
import org.koin.dsl.module

fun dateTimeModule() = module {
    single<DateTimeApi> { KotlinxDateTimeApi() }
}