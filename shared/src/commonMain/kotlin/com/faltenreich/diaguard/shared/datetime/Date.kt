package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.serialization.ObjectInputStream
import com.faltenreich.diaguard.shared.serialization.ObjectOutputStream
import com.faltenreich.diaguard.shared.serialization.Serializable
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

class Date(
    year: Int,
    monthOfYear: Int,
    dayOfMonth: Int,
) : Serializable, Comparable<Date> {

    private var delegate: LocalDate = LocalDate(
        year = year,
        monthNumber = monthOfYear,
        dayOfMonth = dayOfMonth,
    )

    /**
     * Year starting at 0 AD
     */
    val year: Int
        get() = delegate.year

    /**
     * Month-of-year ranging from 1 to 12
     */
    val monthOfYear: Int
        get() = delegate.monthNumber

    /**
     * Day-of-month ranging from 1 to 31, depending on month
     */
    val dayOfMonth: Int
        get() = delegate.dayOfMonth

    private constructor(localDate: LocalDate) : this(
        year = localDate.year,
        monthOfYear = localDate.monthNumber,
        dayOfMonth = localDate.dayOfMonth,
    )

    fun plusDays(days: Int): Date {
        return Date(localDate = delegate.plus(days, DateTimeUnit.DAY))
    }

    fun plusMonths(months: Int): Date {
        return Date(localDate = delegate.plus(months, DateTimeUnit.MONTH))
    }

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

    fun copy(
        year: Int = this.year,
        monthOfYear: Int = this.monthOfYear,
        dayOfMonth: Int = this.dayOfMonth,
    ): Date {
        return Date(
            year = year,
            monthOfYear = monthOfYear,
            dayOfMonth = dayOfMonth,
        )
    }

    override fun compareTo(other: Date): Int {
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
        return other is Date &&
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

    @Suppress("unused")
    private fun readObject(inputStream: ObjectInputStream) {
        delegate = LocalDate.fromEpochDays(inputStream.readLong().toInt())
    }

    @Suppress("unused")
    private fun writeObject(outputStream: ObjectOutputStream) {
        outputStream.writeLong(delegate.toEpochDays().toLong())
    }

    companion object {

        fun today(): Date {
            return DateTime.now().date
        }
    }
}