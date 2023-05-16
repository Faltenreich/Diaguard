package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.datetime.Timeable
import com.faltenreich.diaguard.shared.serialization.ObjectInputStream
import com.faltenreich.diaguard.shared.serialization.ObjectOutputStream
import kotlinx.datetime.LocalTime

class KotlinxTime(
    hourOfDay: Int,
    minuteOfHour: Int,
    secondOfMinute: Int,
    millisOfSecond: Int,
    nanosOfMillis: Int,
) : Timeable {

    private var localTime = LocalTime(
        hour = hourOfDay,
        minute = minuteOfHour,
        second = secondOfMinute,
        nanosecond = millisOfSecond * DateTimeConstants.NANOS_PER_SECOND + nanosOfMillis,
    )

    override val hourOfDay: Int
        get() = localTime.hour

    override val minuteOfHour: Int
        get() = localTime.minute

    override val secondOfMinute: Int
        get() = localTime.second

    override val millisOfSecond: Int
        get() = localTime.nanosecond / DateTimeConstants.NANOS_PER_SECOND

    override val nanosOfMillis: Int
        get() = localTime.nanosecond.mod(DateTimeConstants.NANOS_PER_SECOND)

    private fun readObject(inputStream: ObjectInputStream) {
        localTime = LocalTime.fromNanosecondOfDay(inputStream.readLong())
    }

    private fun writeObject(outputStream: ObjectOutputStream) {
        val nanosOfDay = hourOfDay * DateTimeConstants.NANOS_PER_HOUR +
            minuteOfHour * DateTimeConstants.NANOS_PER_MINUTE +
            secondOfMinute * DateTimeConstants.NANOS_PER_SECOND +
            nanosOfMillis
        outputStream.writeLong(nanosOfDay)
    }
}