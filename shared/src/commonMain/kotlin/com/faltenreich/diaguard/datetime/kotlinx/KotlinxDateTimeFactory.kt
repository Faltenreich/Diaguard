package com.faltenreich.diaguard.datetime.kotlinx

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.Time
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory

class KotlinxDateTimeFactory : DateTimeFactory {

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

    override fun date(isoString: String): Date {
        return KotlinxDate(isoString)
    }

    override fun now(): KotlinxDateTime {
        return KotlinxDateTime.now()
    }
}