package com.faltenreich.diaguard.data.preview

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.DayOfWeek
import com.faltenreich.diaguard.datetime.Time
import com.faltenreich.diaguard.serialization.ObjectInputStream
import com.faltenreich.diaguard.serialization.ObjectOutputStream

class PreviewDate(
    override val year: Int = 2025,
    override val monthNumber: Int = 1,
    override val dayOfMonth: Int = 1,
) : Date {

    override val dayOfWeek: DayOfWeek
        get() = DayOfWeek.MONDAY

    override fun atTime(time: Time): DateTime = PreviewDateTime(date = this)

    override fun atStartOfDay(): DateTime = PreviewDateTime(date = this)

    override fun atEndOfDay(): DateTime = PreviewDateTime(date = this)

    override fun minus(
        value: Int,
        unit: DateUnit
    ): Date = this

    override fun plus(
        value: Int,
        unit: DateUnit
    ): Date = this

    override fun daysBetween(date: Date): Int = 1

    override fun copy(
        year: Int,
        monthNumber: Int,
        dayOfMonth: Int,
    ): Date = PreviewDate(
        year = year,
        monthNumber = monthNumber,
        dayOfMonth = dayOfMonth,
    )

    override fun equals(other: Any?): Boolean = this == other

    override fun hashCode(): Int = 0

    override fun toString(): String = "$year-$monthNumber-$dayOfMonth"

    override fun readObject(inputStream: ObjectInputStream) = Unit

    override fun writeObject(outputStream: ObjectOutputStream) = Unit
}