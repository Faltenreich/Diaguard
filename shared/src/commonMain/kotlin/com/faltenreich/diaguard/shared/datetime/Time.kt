package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.serialization.ObjectInputStream
import com.faltenreich.diaguard.shared.serialization.ObjectOutputStream
import com.faltenreich.diaguard.shared.serialization.Serializable
import org.koin.core.parameter.parametersOf

class Time(
    hourOfDay: Int,
    minuteOfHour: Int,
    secondOfMinute: Int = 0,
    millisOfSecond: Int = 0,
    nanosOfMillis: Int = 0,
) : Serializable, Comparable<Time> {

    private val delegate: Timeable = inject {
        parametersOf(
            hourOfDay,
            minuteOfHour,
            secondOfMinute,
            millisOfSecond,
            nanosOfMillis,
        )
    }

    val hourOfDay: Int
        get() = delegate.hourOfDay

    val minuteOfHour: Int
        get() = delegate.minuteOfHour

    val secondOfMinute: Int
        get() = delegate.secondOfMinute

    val millisOfSecond: Int
        get() = delegate.millisOfSecond

    val nanosOfMilli: Int
        get() = delegate.nanosOfMilli

    fun atDate(date: Date): DateTime {
        return DateTime(
            year = date.year,
            monthOfYear = date.monthOfYear,
            dayOfMonth = date.dayOfMonth,
            hourOfDay = hourOfDay,
            minuteOfHour = minuteOfHour,
            secondOfMinute = secondOfMinute,
            millisOfSecond = millisOfSecond,
            nanosOfMilli = nanosOfMilli,
        )
    }

    override fun compareTo(other: Time): Int {
        return when {
            hourOfDay > other.hourOfDay -> 1
            hourOfDay < other.hourOfDay -> -1
            minuteOfHour > other.minuteOfHour -> 1
            minuteOfHour < other.minuteOfHour -> -1
            secondOfMinute > other.secondOfMinute -> 1
            secondOfMinute < other.secondOfMinute -> -1
            millisOfSecond > other.millisOfSecond -> 1
            millisOfSecond < other.millisOfSecond -> -1
            nanosOfMilli > other.nanosOfMilli -> 1
            nanosOfMilli < other.nanosOfMilli -> -1
            else -> 0
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Time &&
            hourOfDay == other.hourOfDay &&
            minuteOfHour == other.minuteOfHour &&
            secondOfMinute == other.secondOfMinute &&
            millisOfSecond == other.millisOfSecond &&
            nanosOfMilli == other.nanosOfMilli
    }

    override fun hashCode(): Int {
        return hourOfDay.hashCode().times(31) +
            minuteOfHour.hashCode().times(31) +
            secondOfMinute.hashCode().times(31) +
            millisOfSecond.hashCode().times(31) +
            nanosOfMilli.hashCode().times(31)
    }

    override fun toString(): String {
        return "%02d:%02d:%02d.%04d".format(
            hourOfDay,
            minuteOfHour,
            secondOfMinute,
            millisOfSecond,
        )
    }

    @Suppress("unused")
    private fun readObject(inputStream: ObjectInputStream) {
        delegate.readObject(inputStream)
    }

    @Suppress("unused")
    private fun writeObject(outputStream: ObjectOutputStream) {
        delegate.writeObject(outputStream)
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