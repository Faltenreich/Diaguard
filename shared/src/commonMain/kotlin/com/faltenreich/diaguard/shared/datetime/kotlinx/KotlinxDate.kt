package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.Dateable
import com.faltenreich.diaguard.shared.datetime.DayOfWeek
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
    monthOfYear: Int,
    dayOfMonth: Int,
) : Dateable {

    private var delegate: LocalDate = LocalDate(
        year = year,
        monthNumber = monthOfYear,
        dayOfMonth = dayOfMonth,
    )

    private constructor(localDate: LocalDate) : this(
        year = localDate.year,
        monthOfYear = localDate.monthNumber,
        dayOfMonth = localDate.dayOfMonth,
    )

    override val year: Int
        get() = delegate.year

    override val monthOfYear: Int
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

    override fun minusDays(days: Int): Dateable {
        return KotlinxDate(delegate.minus(days, DateTimeUnit.DAY))
    }

    override fun plusDays(days: Int): Dateable {
        return KotlinxDate(delegate.plus(days, DateTimeUnit.DAY))
    }

    override fun minusMonths(months: Int): Dateable {
        return KotlinxDate(delegate.minus(months, DateTimeUnit.MONTH))
    }

    override fun plusMonths(months: Int): Dateable {
        return KotlinxDate(delegate.plus(months, DateTimeUnit.MONTH))
    }

    override fun readObject(inputStream: ObjectInputStream) {
        delegate = LocalDate.fromEpochDays(inputStream.readLong().toInt())
    }

    override fun writeObject(outputStream: ObjectOutputStream) {
        outputStream.writeLong(delegate.toEpochDays().toLong())
    }
}