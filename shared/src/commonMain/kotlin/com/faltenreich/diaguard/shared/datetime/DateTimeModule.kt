package com.faltenreich.diaguard.shared.datetime

import androidx.compose.ui.input.key.Key.Companion.T
import com.faltenreich.diaguard.shared.datetime.kotlinx.KotlinxDate
import com.faltenreich.diaguard.shared.datetime.kotlinx.KotlinxDateTime
import com.faltenreich.diaguard.shared.datetime.kotlinx.KotlinxFactory
import com.faltenreich.diaguard.shared.datetime.kotlinx.KotlinxTime
import org.koin.core.module.dsl.singleOf
import org.koin.core.parameter.ParametersHolder
import org.koin.dsl.module

fun dateTimeModule() = module {
    factory<Dateable> { (year: Int, monthNumber: Int, dayOfMonth: Int) ->
        KotlinxDate(year, monthNumber, dayOfMonth)
    }
    factory<Timeable> { (hourOfDay: Int, minuteOfHour: Int, secondOfMinute: Int, millisOfSecond: Int, nanosOfMilli: Int) ->
        KotlinxTime(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond, nanosOfMilli)
    }
    factory<DateTimeable> { (year: Int, monthNumber: Int, dayOfMonth: Int, hourOfDay: Int, minuteOfHour: Int, secondOfMinute: Int, millisOfSecond: Int, nanosOfMilli: Int) ->
        KotlinxDateTime(year, monthNumber, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond, nanosOfMilli)
    }
    single<DateTimeFactory<*, *, *>> { KotlinxFactory() }
    singleOf(::DateTimeFormatter)
}

inline operator fun <reified T> ParametersHolder.component6(): T = elementAt(5, T::class)
inline operator fun <reified T> ParametersHolder.component7(): T = elementAt(6, T::class)
inline operator fun <reified T> ParametersHolder.component8(): T = elementAt(7, T::class)