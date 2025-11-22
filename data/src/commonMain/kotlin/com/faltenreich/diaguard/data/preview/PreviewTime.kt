package com.faltenreich.diaguard.data.preview

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.Time
import com.faltenreich.diaguard.serialization.ObjectInputStream
import com.faltenreich.diaguard.serialization.ObjectOutputStream

class PreviewTime(
    override val hourOfDay: Int = 0,
    override val minuteOfHour: Int = 0,
    override val secondOfMinute: Int = 0,
    override val millisOfSecond: Int = 0,
    override val nanosOfMilli: Int = 0,
) : Time {

    override fun atDate(date: Date): DateTime = PreviewDateTime(date = date, time = this)

    override fun copy(
        hourOfDay: Int,
        minuteOfHour: Int,
        secondOfMinute: Int,
        millisOfSecond: Int,
        nanosOfMilli: Int
    ): Time = PreviewTime(
        hourOfDay = hourOfDay,
        minuteOfHour = minuteOfHour,
        secondOfMinute = secondOfMinute,
        millisOfSecond = millisOfSecond,
        nanosOfMilli = nanosOfMilli,
    )

    override fun equals(other: Any?): Boolean = this == other

    override fun hashCode(): Int = 0

    override fun toString(): String = "$hourOfDay:$minuteOfHour:$secondOfMinute"

    override fun readObject(inputStream: ObjectInputStream) = Unit

    override fun writeObject(outputStream: ObjectOutputStream) = Unit
}