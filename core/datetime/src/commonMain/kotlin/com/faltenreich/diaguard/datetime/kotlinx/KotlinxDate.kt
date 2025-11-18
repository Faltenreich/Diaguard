@file:Suppress("MagicNumber")

package com.faltenreich.diaguard.datetime.kotlinx

import com.faltenreich.diaguard.serialization.ObjectInputStream
import com.faltenreich.diaguard.serialization.ObjectOutputStream
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.DayOfWeek
import com.faltenreich.diaguard.datetime.Time
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus

class KotlinxDate(private var delegate: LocalDate) : Date {

    override val year: Int get() = delegate.year
    override val monthNumber: Int get() = delegate.month.number
    override val dayOfMonth: Int get() = delegate.day
    override val dayOfWeek: DayOfWeek get() = delegate.dayOfWeek.toDomain()

    constructor(
        year: Int,
        monthNumber: Int,
        dayOfMonth: Int,
    ) : this(
        LocalDate(
            year = year,
            month = monthNumber,
            day = dayOfMonth,
        )
    )

    constructor(isoString: String) : this(LocalDate.parse(isoString))

    override fun atTime(time: Time): DateTime {
        return KotlinxDateTime(
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

    override fun atStartOfDay(): DateTime {
        return KotlinxDateTime(
            year = year,
            monthNumber = monthNumber,
            dayOfMonth = dayOfMonth,
            hourOfDay = 0,
            minuteOfHour = 0,
            secondOfMinute = 0,
            millisOfSecond = 0,
            nanosOfMilli = 0,
        )
    }

    override fun atEndOfDay(): DateTime {
        return KotlinxDateTime(
            year = year,
            monthNumber = monthNumber,
            dayOfMonth = dayOfMonth,
            hourOfDay = 23,
            minuteOfHour = 59,
            secondOfMinute = 59,
            millisOfSecond = 999,
            nanosOfMilli = 999,
        )
    }

    override fun minus(value: Int, unit: DateUnit): Date {
        return KotlinxDate(delegate.minus(value, unit.fromDomain()))
    }

    override fun plus(value: Int, unit: DateUnit): Date {
        return KotlinxDate(delegate.plus(value, unit.fromDomain()))
    }

    override fun daysBetween(date: Date): Int {
        return delegate.daysUntil(
            LocalDate(
                year = date.year,
                month = date.monthNumber,
                day = date.dayOfMonth,
            )
        )
    }

    override fun copy(year: Int, monthNumber: Int, dayOfMonth: Int): Date {
        return KotlinxDate(
            year = year,
            monthNumber = monthNumber,
            dayOfMonth = dayOfMonth,
        )
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
        return delegate.toString()
    }

    override fun readObject(inputStream: ObjectInputStream) {
        delegate = LocalDate.fromEpochDays(inputStream.readLong().toInt())
    }

    override fun writeObject(outputStream: ObjectOutputStream) {
        outputStream.writeLong(delegate.toEpochDays())
    }
}