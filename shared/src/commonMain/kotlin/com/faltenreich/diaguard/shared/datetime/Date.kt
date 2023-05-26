package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.datetime.kotlinx.KotlinxDate
import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.serialization.Serializable

class Date(
    year: Int,
    monthOfYear: Int,
    dayOfMonth: Int,
) : Dateable by KotlinxDate(
    year = year,
    monthOfYear = monthOfYear,
    dayOfMonth = dayOfMonth,
), Serializable, Comparable<Dateable> {

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

    override fun compareTo(other: Dateable): Int {
        return when {
            year > other.year -> 1
            year < other.year -> -1
            monthOfYear > other.monthOfYear -> 1
            monthOfYear < other.monthOfYear -> -1
            dayOfMonth > other.dayOfMonth -> 1
            dayOfMonth < other.dayOfMonth -> -1
            else -> 0
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Dateable &&
            year == other.year &&
            monthOfYear == other.monthOfYear &&
            dayOfMonth == other.dayOfMonth
    }

    override fun hashCode(): Int {
        return year.hashCode().times(31) +
            monthOfYear.hashCode().times(31) +
            dayOfMonth.hashCode().times(31)
    }

    override fun toString(): String {
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