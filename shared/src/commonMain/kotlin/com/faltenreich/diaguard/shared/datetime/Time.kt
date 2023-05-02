package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.primitive.format
import kotlinx.datetime.LocalTime

class Time(
    hourOfDay: Int,
    minuteOfHour: Int,
    secondOfMinute: Int = 0,
    millisOfSecond: Int = 0,
    nanosOfMillis: Int = 0,
) {

    private val localTime = LocalTime(
        hour = hourOfDay,
        minute = minuteOfHour,
        second = secondOfMinute,
        nanosecond = millisOfSecond * DateTimeConstants.NANOS_PER_SECOND + nanosOfMillis,
    )

    /**
     * Hour-of-day ranging from 0 to 24
     */
    val hourOfDay: Int
        get() = localTime.hour

    /**
     * Minute-of-hour ranging from 0 to 60
     */
    val minuteOfHour: Int
        get() = localTime.minute

    /**
     * Second-of-minute ranging from 0 to 60
     */
    val secondOfMinute: Int
        get() = localTime.second

    /**
     * Milliseconds-of-second ranging from 0 to 1,000
     */
    val millisOfSecond: Int
        get() = localTime.nanosecond / DateTimeConstants.NANOS_PER_SECOND

    /**
     * Nanoseconds-of-millisecond ranging from 0 to 1,000
     */
    val nanosOfMillis: Int
        get() = localTime.nanosecond.mod(DateTimeConstants.NANOS_PER_SECOND)

    fun atDate(date: Date): DateTime {
        return DateTime(
            year = date.year,
            monthOfYear = date.monthOfYear,
            dayOfMonth = date.dayOfMonth,
            hourOfDay = hourOfDay,
            minuteOfHour = minuteOfHour,
            secondOfMinute = secondOfMinute,
            millisOfSecond = millisOfSecond,
            nanosOfMillis = nanosOfMillis,
        )
    }

    fun localized(): String {
        return "%02d:%02d".format(
            hourOfDay,
            minuteOfHour,
        )
    }

    companion object {

        fun now(): Time {
            return DateTime.now().time
        }

        fun atMidnight(): Time {
            return Time(
                hourOfDay = 0,
                minuteOfHour = 0,
                secondOfMinute = 0,
                millisOfSecond = 0,
                nanosOfMillis = 0,
            )
        }
    }
}