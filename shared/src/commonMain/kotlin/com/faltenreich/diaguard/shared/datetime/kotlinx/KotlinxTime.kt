package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.serialization.ObjectInputStream
import com.faltenreich.diaguard.shared.serialization.ObjectOutputStream
import kotlinx.datetime.LocalTime

class KotlinxTime(
    hourOfDay: Int,
    minuteOfHour: Int,
    secondOfMinute: Int = 0,
    millisOfSecond: Int = 0,
    nanosOfMilli: Int = 0,
) : Time {

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

    override fun copy(
        hourOfDay: Int,
        minuteOfHour: Int,
        secondOfMinute: Int,
        millisOfSecond: Int,
        nanosOfMilli: Int
    ): Time {
        return KotlinxTime(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond, nanosOfMilli)
    }

    override fun compareTo(other: Time): Int {
        return when {
            hourOfDay > other.hourOfDay -> 1
            hourOfDay < other.hourOfDay -> -1
            minuteOfHour > other.minuteOfHour -> 1
            minuteOfHour < other.minuteOfHour -> -1
            secondOfMinute > other.secondOfMinute -> 1
            secondOfMinute < other.secondOfMinute -> -1
            millisOfSecond > other.millisOfSecond -> 1
            millisOfSecond < other.millisOfSecond -> -1
            nanosOfMilli > other.nanosOfMilli -> 1
            nanosOfMilli < other.nanosOfMilli -> -1
            else -> 0
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Time &&
            hourOfDay == other.hourOfDay &&
            minuteOfHour == other.minuteOfHour &&
            secondOfMinute == other.secondOfMinute &&
            millisOfSecond == other.millisOfSecond &&
            nanosOfMilli == other.nanosOfMilli
    }

    override fun hashCode(): Int {
        return hourOfDay.hashCode().times(31) +
            minuteOfHour.hashCode().times(31) +
            secondOfMinute.hashCode().times(31) +
            millisOfSecond.hashCode().times(31) +
            nanosOfMilli.hashCode().times(31)
    }

    override fun toString(): String {
        return "%02d:%02d:%02d.%04d".format(
            hourOfDay,
            minuteOfHour,
            secondOfMinute,
            millisOfSecond,
        )
    }
}