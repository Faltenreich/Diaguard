package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.datetime.LocalDateTime

class KotlinxDateTime(private val localDateTime: LocalDateTime) : DateTime {

    override val year: Int
        get() = localDateTime.year

    override val monthOfYear: Int
        get() = localDateTime.monthNumber

    override val dayOfMonth: Int
        get() = localDateTime.dayOfMonth

    override val hourOfDay: Int
        get() = localDateTime.hour

    override val minuteOfHour: Int
        get() = localDateTime.minute

    override val secondOfMinute: Int
        get() = localDateTime.second

    override val millisOfSecond: Int
        get() = localDateTime.nanosecond / NANO_SECONDS_PER_SECOND

    override val nanosOfMillis: Int
        get() = localDateTime.nanosecond.mod(NANO_SECONDS_PER_SECOND)

    override fun toString(): String {
        return localDateTime.toString()
    }

    companion object {

        private const val NANO_SECONDS_PER_SECOND = 1_000
    }
}