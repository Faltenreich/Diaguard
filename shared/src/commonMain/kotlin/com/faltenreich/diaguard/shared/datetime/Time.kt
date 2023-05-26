package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.datetime.kotlinx.KotlinxTime
import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.serialization.Serializable

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
), Serializable, Comparable<Timeable> {

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

    override fun compareTo(other: Timeable): Int {
        return when {
            hourOfDay > other.hourOfDay -> 1
            hourOfDay < other.hourOfDay -> -1
            minuteOfHour > other.minuteOfHour -> 1
            minuteOfHour < other.minuteOfHour -> -1
            secondOfMinute > other.secondOfMinute -> 1
            secondOfMinute < other.secondOfMinute -> -1
            millisOfSecond > other.millisOfSecond -> 1
            millisOfSecond < other.millisOfSecond -> -1
            nanosOfMillis > other.nanosOfMillis -> 1
            nanosOfMillis < other.nanosOfMillis -> -1
            else -> 0
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Timeable &&
            hourOfDay == other.hourOfDay &&
            minuteOfHour == other.minuteOfHour &&
            secondOfMinute == other.secondOfMinute &&
            millisOfSecond == other.millisOfSecond &&
            nanosOfMillis == other.nanosOfMillis
    }

    override fun hashCode(): Int {
        return hourOfDay.hashCode().times(31) +
            minuteOfHour.hashCode().times(31) +
            secondOfMinute.hashCode().times(31) +
            millisOfSecond.hashCode().times(31) +
            nanosOfMillis.hashCode().times(31)
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