package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.datetime.kotlinx.KotlinxDate
import com.faltenreich.diaguard.shared.primitive.format

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

    fun localized(): String {
        return "%02d.%02d.%04d".format(
            dayOfMonth,
            monthOfYear,
            year,
        )
    }

    companion object {

        fun today(): Date {
            return DateTime.now().date
        }
    }
}