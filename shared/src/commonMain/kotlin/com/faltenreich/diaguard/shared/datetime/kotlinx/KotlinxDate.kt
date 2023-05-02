package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.datetime.Time
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atTime

class KotlinxDate(private val localDate: LocalDate) : Date {

    override val year: Int
        get() = localDate.year

    override val monthOfYear: Int
        get() = localDate.monthNumber

    override val dayOfMonth: Int
        get() = localDate.dayOfMonth

    override fun atTime(time: Time): DateTime {
        val localTime = LocalTime(
            hour = time.hourOfDay,
            minute = time.minuteOfHour,
            second = time.secondOfMinute,
            nanosecond = time.millisOfSecond * DateTimeConstants.NANOS_PER_SECOND + time.nanosOfMillis,
        )
        val localDateTime = localDate.atTime(localTime)
        return KotlinxDateTime(localDateTime)
    }

    override fun toString(): String {
        return localDate.toString()
    }
}