package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DayOfWeek
import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.serialization.ObjectInputStream
import com.faltenreich.diaguard.shared.serialization.ObjectOutputStream
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek.FRIDAY
import kotlinx.datetime.DayOfWeek.MONDAY
import kotlinx.datetime.DayOfWeek.SATURDAY
import kotlinx.datetime.DayOfWeek.SUNDAY
import kotlinx.datetime.DayOfWeek.THURSDAY
import kotlinx.datetime.DayOfWeek.TUESDAY
import kotlinx.datetime.DayOfWeek.WEDNESDAY
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

class KotlinxDate(
    year: Int,
    monthNumber: Int,
    dayOfMonth: Int,
) : Date {

    private var delegate: LocalDate = LocalDate(
        year = year,
        monthNumber = monthNumber,
        dayOfMonth = dayOfMonth,
    )

    private constructor(localDate: LocalDate) : this(
        year = localDate.year,
        monthNumber = localDate.monthNumber,
        dayOfMonth = localDate.dayOfMonth,
    )

    override val year: Int
        get() = delegate.year

    override val monthNumber: Int
        get() = delegate.monthNumber

    override val dayOfMonth: Int
        get() = delegate.dayOfMonth

    override val dayOfWeek: DayOfWeek
        get() = when (delegate.dayOfWeek) {
            MONDAY -> DayOfWeek.MONDAY
            TUESDAY -> DayOfWeek.TUESDAY
            WEDNESDAY -> DayOfWeek.WEDNESDAY
            THURSDAY -> DayOfWeek.THURSDAY
            FRIDAY -> DayOfWeek.FRIDAY
            SATURDAY -> DayOfWeek.SATURDAY
            SUNDAY -> DayOfWeek.SUNDAY
            else -> throw IllegalStateException("Unknown dayOfWeek: ${delegate.dayOfWeek}")
        }

    override fun minusDays(days: Int): Date {
        return KotlinxDate(delegate.minus(days, DateTimeUnit.DAY))
    }

    override fun plusDays(days: Int): Date {
        return KotlinxDate(delegate.plus(days, DateTimeUnit.DAY))
    }

    override fun minusMonths(months: Int): Date {
        return KotlinxDate(delegate.minus(months, DateTimeUnit.MONTH))
    }

    override fun plusMonths(months: Int): Date {
        return KotlinxDate(delegate.plus(months, DateTimeUnit.MONTH))
    }

    override fun readObject(inputStream: ObjectInputStream) {
        delegate = LocalDate.fromEpochDays(inputStream.readLong().toInt())
    }

    override fun writeObject(outputStream: ObjectOutputStream) {
        outputStream.writeLong(delegate.toEpochDays().toLong())
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
}