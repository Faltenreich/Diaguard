package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.architecture.Mapper
import kotlinx.datetime.LocalDateTime

class KotlinxDateTimeMapper : Mapper<LocalDateTime, DateTime> {

    override fun map(input: LocalDateTime): DateTime {
        return DateTime(
            year = input.year,
            monthOfYear = input.monthNumber,
            dayOfMonth = input.dayOfMonth,
            hourOfDay = input.hour,
            minuteOfHour = input.minute,
            secondOfMinute = input.second,
            millisOfSecond = input.nanosecond / NANO_SECONDS_PER_SECOND,
            nanosOfMillis = input.nanosecond.mod(NANO_SECONDS_PER_SECOND),
        )
    }

    companion object {

        private const val NANO_SECONDS_PER_SECOND = 1_000
    }
}