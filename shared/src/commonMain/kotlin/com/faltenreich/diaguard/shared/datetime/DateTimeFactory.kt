package com.faltenreich.diaguard.shared.datetime

interface DateTimeFactory {

    fun now(): DateTimeable

    fun date(year: Int, monthNumber: Int, dayOfMonth: Int): Date

    fun time(
        hourOfDay: Int,
        minuteOfHour: Int,
        secondOfMinute: Int = 0,
        millisOfSecond: Int = 0,
        nanosOfMilli: Int = 0,
    ): Time

    fun today(): Date

    fun fromIsoString(isoString: String): DateTimeable

    fun fromMillis(millis: Long): DateTimeable
}