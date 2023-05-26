package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.datetime.Timeable
import com.faltenreich.diaguard.shared.serialization.ObjectInputStream
import com.faltenreich.diaguard.shared.serialization.ObjectOutputStream
import kotlinx.datetime.LocalTime

class KotlinxTime(
    hourOfDay: Int,
    minuteOfHour: Int,
    secondOfMinute: Int = 0,
    millisOfSecond: Int = 0,
    nanosOfMilli: Int = 0,
) : Timeable {

    private var delegate: LocalTime = LocalTime(
        hour = hourOfDay,
        minute = minuteOfHour,
        second = secondOfMinute,
        nanosecond = millisOfSecond * DateTimeConstants.NANOS_PER_SECOND + nanosOfMilli,
    )

    override val hourOfDay: Int
        get() = delegate.hour

    override val minuteOfHour: Int
        get() = delegate.minute

    override val secondOfMinute: Int
        get() = delegate.second

    override val millisOfSecond: Int
        get() = delegate.nanosecond / DateTimeConstants.NANOS_PER_SECOND

    override val nanosOfMilli: Int
        get() = delegate.nanosecond.mod(DateTimeConstants.NANOS_PER_SECOND)

    override fun readObject(inputStream: ObjectInputStream) {
        delegate = LocalTime.fromNanosecondOfDay(inputStream.readLong())
    }

    override fun writeObject(outputStream: ObjectOutputStream) {
        val nanosOfDay = hourOfDay * DateTimeConstants.NANOS_PER_HOUR +
            minuteOfHour * DateTimeConstants.NANOS_PER_MINUTE +
            secondOfMinute * DateTimeConstants.NANOS_PER_SECOND +
            nanosOfMilli
        outputStream.writeLong(nanosOfDay)
    }
}