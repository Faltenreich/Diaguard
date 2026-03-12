@file:Suppress("MagicNumber")

package com.faltenreich.diaguard.datetime

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.datetime.format.FormatDateTimeUseCase
import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDateTimeFactory
import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDateTimeFormatter
import com.faltenreich.diaguard.localization.localizationModule
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun dateTimeModule() = module {
    includes(localizationModule())
    includes(dateTimePlatformModule())

    factoryOf(::KotlinxDateTimeFactory) bind DateTimeFactory::class
    factoryOf(::KotlinxDateTimeFormatter) bind DateTimeFormatter::class

    factoryOf(::FormatDateTimeUseCase)
    factoryOf(::GetTodayUseCase)
}

expect fun dateTimePlatformModule(): Module