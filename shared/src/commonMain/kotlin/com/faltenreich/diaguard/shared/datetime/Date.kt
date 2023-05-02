package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.primitive.format
import kotlinx.datetime.LocalDate

class Date(
    year: Int,
    monthOfYear: Int,
    dayOfMonth: Int,
) {

    private val localDate = LocalDate(
        year = year,
        monthNumber = monthOfYear,
        dayOfMonth = dayOfMonth,
    )

    /**
     * Year starting at 0 AD
     */
    val year: Int
        get() = localDate.year

    /**
     * Month-of-year ranging from 1 to 12
     */
    val monthOfYear: Int
        get() = localDate.monthNumber

    /**
     * Day-of-month ranging from 1 to 31, depending on month
     */
    val dayOfMonth: Int
        get() = localDate.dayOfMonth

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