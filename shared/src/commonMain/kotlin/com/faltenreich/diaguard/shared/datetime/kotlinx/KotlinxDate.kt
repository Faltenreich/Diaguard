package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.DateUnit
import com.faltenreich.diaguard.shared.datetime.DayOfWeek
import com.faltenreich.diaguard.shared.datetime.Time
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
import kotlinx.datetime.DateTimeUnit as KotlinxDateTimeUnit

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

    private fun DateUnit.toKotlinx(): DateTimeUnit.DateBased {
        return when (this) {
            DateUnit.DAY -> KotlinxDateTimeUnit.DAY
            DateUnit.WEEK -> KotlinxDateTimeUnit.WEEK
            DateUnit.MONTH -> KotlinxDateTimeUnit.MONTH
            DateUnit.QUARTER -> KotlinxDateTimeUnit.QUARTER
            DateUnit.YEAR -> KotlinxDateTimeUnit.YEAR
            DateUnit.CENTURY -> KotlinxDateTimeUnit.CENTURY
        }
    }

    override fun minus(value: Int, unit: DateUnit): Date {
        return KotlinxDate(delegate.minus(value, unit.toKotlinx()))
    }

    override fun plus(value: Int, unit: DateUnit): Date {
        return KotlinxDate(delegate.plus(value, unit.toKotlinx()))
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
        return "%02d.%02d.%04d".format(
            dayOfMonth,
            monthNumber,
            year,
        )
    }

    override fun readObject(inputStream: ObjectInputStream) {
        delegate = LocalDate.fromEpochDays(inputStream.readLong().toInt())
    }

    override fun writeObject(outputStream: ObjectOutputStream) {
        outputStream.writeLong(delegate.toEpochDays().toLong())
    }
}