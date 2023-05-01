package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.Time
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atDate

class KotlinxTime(private val localTime: LocalTime) : Time {

    override val hourOfDay: Int
        get() = localTime.hour

    override val minuteOfHour: Int
        get() = localTime.minute

    override val secondOfMinute: Int
        get() = localTime.second

    override val millisOfSecond: Int
        get() = localTime.nanosecond / NANO_SECONDS_PER_SECOND

    override val nanosOfMillis: Int
        get() = localTime.nanosecond.mod(NANO_SECONDS_PER_SECOND)

    override fun atDate(date: Date): DateTime {
        val localDate = LocalDate(
            year = date.year,
            monthNumber = date.monthOfYear,
            dayOfMonth = date.dayOfMonth,
        )
        val localDateTime = localTime.atDate(localDate)
        return KotlinxDateTime(localDateTime)
    }

    override fun toString(): String {
        return localTime.toString()
    }

    companion object {

        const val NANO_SECONDS_PER_SECOND = 1_000
    }
}