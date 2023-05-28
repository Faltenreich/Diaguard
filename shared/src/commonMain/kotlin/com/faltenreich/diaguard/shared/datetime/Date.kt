package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.serialization.ObjectInputStream
import com.faltenreich.diaguard.shared.serialization.ObjectOutputStream
import com.faltenreich.diaguard.shared.serialization.Serializable
import org.koin.core.parameter.parametersOf

class Date(
    year: Int,
    monthNumber: Int,
    dayOfMonth: Int,
) : Serializable, Comparable<Date> {

    private val delegate: Dateable = inject { parametersOf(year, monthNumber, dayOfMonth) }

    val year: Int
        get() = delegate.year

    val monthNumber: Int
        get() = delegate.monthNumber

    val month: Month
        get() = delegate.month

    val monthOfYear: MonthOfYear
        get() = delegate.monthOfYear

    val dayOfMonth: Int
        get() = delegate.dayOfMonth

    val dayOfWeek: DayOfWeek
        get() = delegate.dayOfWeek

    private constructor(dateable: Dateable) : this(
        year = dateable.year,
        monthNumber = dateable.monthNumber,
        dayOfMonth = dateable.dayOfMonth,
    )

    fun minusDays(days: Int): Date {
        return Date(delegate.minusDays(days))
    }

    fun plusDays(days: Int): Date {
        return Date(delegate.plusDays(days))
    }

    fun minusMonths(months: Int): Date {
        return Date(delegate.minusMonths(months))
    }

    fun plusMonths(months: Int): Date {
        return Date(delegate.plusMonths(months))
    }

    fun atTime(time: Time): DateTime {
        return DateTime(
            year = year,
            monthNumber = monthNumber,
            dayOfMonth = dayOfMonth,
            hourOfDay = time.hourOfDay,
            minuteOfHour = time.minuteOfHour,
            secondOfMinute = time.secondOfMinute,
            millisOfSecond = time.millisOfSecond,
            nanosOfMilli = time.nanosOfMilli,
        )
    }

    override fun compareTo(other: Date): Int {
        return when {
            year > other.year -> 1
            year < other.year -> -1
            monthNumber > other.monthNumber -> 1
            monthNumber < other.monthNumber -> -1
            dayOfMonth > other.dayOfMonth -> 1
            dayOfMonth < other.dayOfMonth -> -1
            else -> 0
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Date &&
            year == other.year &&
            monthNumber == other.monthNumber &&
            dayOfMonth == other.dayOfMonth
    }

    override fun hashCode(): Int {
        return year.hashCode().times(31) +
            monthNumber.hashCode().times(31) +
            dayOfMonth.hashCode().times(31)
    }

    override fun toString(): String {
        return "%02d.%02d.%04d".format(
            dayOfMonth,
            monthNumber,
            year,
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

        fun today(): Date {
            return DateTime.now().date
        }
    }
}