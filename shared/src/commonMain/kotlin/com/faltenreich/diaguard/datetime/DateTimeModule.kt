@file:Suppress("MagicNumber")

package com.faltenreich.diaguard.datetime

import androidx.compose.ui.input.key.Key.Companion.T
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.datetime.format.FormatDateTimeUseCase
import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDateTimeFactory
import org.koin.core.module.dsl.singleOf
import org.koin.core.parameter.ParametersHolder
import org.koin.dsl.module

fun dateTimeModule() = module {
    single<DateTimeFactory> { KotlinxDateTimeFactory() }

    singleOf(::DateTimeFormatter)

    singleOf(::FormatDateTimeUseCase)
    singleOf(::GetTodayUseCase)
}

inline operator fun <reified T> ParametersHolder.component6(): T = elementAt(5, T::class)
inline operator fun <reified T> ParametersHolder.component7(): T = elementAt(6, T::class)
inline operator fun <reified T> ParametersHolder.component8(): T = elementAt(7, T::class)