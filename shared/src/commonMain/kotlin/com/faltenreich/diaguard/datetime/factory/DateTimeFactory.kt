package com.faltenreich.diaguard.datetime.factory

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
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
        secondOfMinute: Int,
        millisOfSecond: Int,
        nanosOfMilli: Int,
    ): DateTime

    fun dateTime(millis: Long): DateTime

    fun dateTime(isoString: String): DateTime

    fun now(): DateTime

    fun today(): Date {
        return now().date
    }
}