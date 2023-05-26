package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.Dateable
import com.faltenreich.diaguard.shared.serialization.ObjectInputStream
import com.faltenreich.diaguard.shared.serialization.ObjectOutputStream
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
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

    override fun plusDays(days: Int): Dateable {
        return KotlinxDate(delegate.plus(days, DateTimeUnit.DAY))
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