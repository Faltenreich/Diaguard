package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.datetime.kotlinx.KotlinxDate

class Date(
    year: Int,
    monthOfYear: Int,
    dayOfMonth: Int,
) : Dateable by KotlinxDate(
    year = year,
    monthOfYear = monthOfYear,
    dayOfMonth = dayOfMonth,
) {

    fun atTime(time: Time): DateTime {
        return DateTime(
            year = year,
            monthOfYear = monthOfYear,
            dayOfMonth = dayOfMonth,
            hourOfDay = time.hourOfDay,
            minuteOfHour = time.minuteOfHour,
            secondOfMinute = time.secondOfMinute,
            millisOfSecond = time.millisOfSecond,
            nanosOfMillis = time.nanosOfMillis,
        )
    }

    companion object {

        fun today(): Date {
            return DateTime.now().date
        }
    }
}