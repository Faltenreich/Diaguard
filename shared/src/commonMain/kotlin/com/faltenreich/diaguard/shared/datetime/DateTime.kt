package com.faltenreich.diaguard.shared.datetime

/**
 * Temporal interface representing local date and time
 */
data class DateTime(

    /**
     * Year starting at 0 AD
     */
    val year: Int,

    /**
     * Month-of-year ranging from 1 to 12
     */
    val monthOfYear: Int,

    /**
     * Day-of-month ranging from 1 to 31, depending on month
     */
    val dayOfMonth: Int,

    /**
     * Hour-of-day ranging from 0 to 24
     */
    val hourOfDay: Int,

    /**
     * Minute-of-hour ranging from 0 to 60
     */
    val minuteOfHour: Int,

    /**
     * Second-of-minute ranging from 0 to 60
     */
    val secondOfMinute: Int,

    /**
     * Milliseconds-of-second ranging from 0 to 1,000
     */
    val millisOfSecond: Int,

    /**
     * Nanoseconds-of-millisecond ranging from 0 to 1,000
     */
    val nanosOfMillis: Int,
)