@file:Suppress("MagicNumber")

package com.faltenreich.diaguard.datetime

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.datetime.format.FormatDateTimeUseCase
import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDateTimeFactory
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun dateTimeModule() = module {
    factoryOf(::KotlinxDateTimeFactory) bind DateTimeFactory::class

    factoryOf(::DateTimeFormatter)

    factoryOf(::FormatDateTimeUseCase)
    factoryOf(::GetTodayUseCase)
}