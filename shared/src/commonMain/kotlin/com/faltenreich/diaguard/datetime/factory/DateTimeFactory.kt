package com.faltenreich.diaguard.datetime.factory

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateProgression
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.Time

interface DateTimeFactory {

    fun date(
        year: Int,
        monthNumber: Int,
        dayOfMonth: Int,
    ): Date

    fun time(
        hourOfDay: Int,
        minuteOfHour: Int,
        secondOfMinute: Int = 0,
        millisOfSecond: Int = 0,
        nanosOfMilli: Int = 0,
    ): Time

    fun dateTime(
        year: Int,
        monthNumber: Int,
        dayOfMonth: Int,
        hourOfDay: Int,
        minuteOfHour: Int,
        secondOfMinute: Int = 0,
        millisOfSecond: Int = 0,
        nanosOfMilli: Int = 0,
    ): DateTime

    fun dateTime(millis: Long): DateTime

    fun dateTimeFromEpoch(epochMillis: Long): DateTime

    fun dateTime(isoString: String): DateTime

    fun date(isoString: String): Date

    fun now(): DateTime

    fun today(): Date {
        return now().date
    }

    fun week(): DateProgression {
        val today = today()
        return DateProgression(
            start = today
                .minus(1, DateUnit.WEEK)
                .plus(1, DateUnit.DAY),
            endInclusive = today,
        )
    }
}