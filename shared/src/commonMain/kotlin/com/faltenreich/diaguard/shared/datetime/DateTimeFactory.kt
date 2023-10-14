package com.faltenreich.diaguard.shared.datetime

interface DateTimeFactory {

    fun now(): DateTime

    fun date(year: Int, monthNumber: Int, dayOfMonth: Int): Date

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

    fun today(): Date

    fun fromIsoString(isoString: String): DateTime

    fun fromMillis(millis: Long): DateTime
}