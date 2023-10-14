package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.datetime.Time

class KotlinxFactory : DateTimeFactory {

    override fun date(
        year: Int,
        monthNumber: Int,
        dayOfMonth: Int,
    ): Date {
        return KotlinxDate(
            year,
            monthNumber,
            dayOfMonth,
        )
    }

    override fun time(
        hourOfDay: Int,
        minuteOfHour: Int,
        secondOfMinute: Int,
        millisOfSecond: Int,
        nanosOfMilli: Int,
    ): Time {
        return KotlinxTime(
            hourOfDay,
            minuteOfHour,
            secondOfMinute,
            millisOfSecond,
            nanosOfMilli,
        )
    }

    override fun dateTime(
        year: Int,
        monthNumber: Int,
        dayOfMonth: Int,
        hourOfDay: Int,
        minuteOfHour: Int,
        secondOfMinute: Int,
        millisOfSecond: Int,
        nanosOfMilli: Int,
    ): DateTime {
        return KotlinxDateTime(
            year,
            monthNumber,
            dayOfMonth,
            hourOfDay,
            minuteOfHour,
            secondOfMinute,
            millisOfSecond,
            nanosOfMilli
        )
    }

    override fun dateTime(millis: Long): DateTime {
        return KotlinxDateTime(millis)
    }

    override fun dateTime(isoString: String): DateTime {
        return KotlinxDateTime(isoString)
    }

    override fun now(): KotlinxDateTime {
        return KotlinxDateTime.now()
    }
}