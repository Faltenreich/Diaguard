package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.datetime.kotlinx.KotlinxTime
import com.faltenreich.diaguard.shared.primitive.format

class Time(
    hourOfDay: Int,
    minuteOfHour: Int,
    secondOfMinute: Int = 0,
    millisOfSecond: Int = 0,
    nanosOfMillis: Int = 0,
) : Timeable by KotlinxTime(
    hourOfDay = hourOfDay,
    minuteOfHour = minuteOfHour,
    secondOfMinute = secondOfMinute,
    millisOfSecond = millisOfSecond,
    nanosOfMillis = nanosOfMillis,
) {

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

    override fun toString(): String {
        return "%02d:%02d:%02d.%04d".format(
            hourOfDay,
            minuteOfHour,
            secondOfMinute,
            millisOfSecond,
        )
    }

    companion object {

        fun now(): Time {
            return DateTime.now().time
        }

        fun atStartOfDay(): Time {
            return Time(
                hourOfDay = 0,
                minuteOfHour = 0,
                secondOfMinute = 0,
                millisOfSecond = 0,
                nanosOfMillis = 0,
            )
        }

        fun atEndOfDay(): Time {
            return Time(
                hourOfDay = 23,
                minuteOfHour = 59,
                secondOfMinute = 59,
                millisOfSecond = 999,
                nanosOfMillis = 999,
            )
        }
    }
}