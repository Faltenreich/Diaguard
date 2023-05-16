package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.Dateable
import com.faltenreich.diaguard.shared.serialization.ObjectInputStream
import com.faltenreich.diaguard.shared.serialization.ObjectOutputStream
import kotlinx.datetime.LocalDate

class KotlinxDate(
    year: Int,
    monthOfYear: Int,
    dayOfMonth: Int,
) : Dateable {

    private var localDate = LocalDate(
        year = year,
        monthNumber = monthOfYear,
        dayOfMonth = dayOfMonth,
    )

    override val year: Int
        get() = localDate.year

    override val monthOfYear: Int
        get() = localDate.monthNumber

    override val dayOfMonth: Int
        get() = localDate.dayOfMonth

    private fun readObject(inputStream: ObjectInputStream) {
        localDate = LocalDate.fromEpochDays(inputStream.readLong().toInt())
    }

    private fun writeObject(outputStream: ObjectOutputStream) {
        outputStream.writeLong(localDate.toEpochDays().toLong())
    }
}