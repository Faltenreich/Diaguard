package com.faltenreich.diaguard.data.preview

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.Time
import com.faltenreich.diaguard.datetime.TimeUnit
import com.faltenreich.diaguard.serialization.ObjectInputStream
import com.faltenreich.diaguard.serialization.ObjectOutputStream
import kotlin.time.Duration

class PreviewDateTime(
    override val date: Date = PreviewDate(),
    override val time: Time = PreviewTime(),
) : DateTime {

    override val millisSince1970: Long
        get() = TODO("Not yet implemented")
    override val epochMilliseconds: Long
        get() = TODO("Not yet implemented")
    override val isoString: String
        get() = TODO("Not yet implemented")

    override fun until(
        other: DateTime,
        unit: TimeUnit
    ): Duration = TODO("Not yet implemented")

    override fun copy(
        year: Int,
        monthNumber: Int,
        dayOfMonth: Int,
        hourOfDay: Int,
        minuteOfHour: Int,
        secondOfMinute: Int,
        millisOfSecond: Int,
        nanosOfMilli: Int
    ): DateTime = PreviewDateTime(
        date = PreviewDate(
            year = year,
            monthNumber = monthNumber,
            dayOfMonth = dayOfMonth,
        ),
        time = PreviewTime(
            hourOfDay = hourOfDay,
            minuteOfHour = minuteOfHour,
            secondOfMinute = secondOfMinute,
            millisOfSecond = millisOfSecond,
            nanosOfMilli = nanosOfMilli,
        )
    )

    override fun equals(other: Any?): Boolean = this == other

    override fun hashCode(): Int = 0

    override fun toString(): String = "$date $time"

    override fun readObject(inputStream: ObjectInputStream) = Unit

    override fun writeObject(outputStream: ObjectOutputStream) = Unit
}