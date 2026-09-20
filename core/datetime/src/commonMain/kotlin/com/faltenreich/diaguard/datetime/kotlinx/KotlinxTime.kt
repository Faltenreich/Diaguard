package com.faltenreich.diaguard.datetime.kotlinx

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.DateTimeConstants
import com.faltenreich.diaguard.datetime.Time
import com.faltenreich.diaguard.datetime.TimeUnit
import com.faltenreich.diaguard.serialization.ObjectInputStream
import com.faltenreich.diaguard.serialization.ObjectOutputStream
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock

internal class KotlinxTime(private var delegate: LocalTime) : Time {

    override val hourOfDay: Int get() = delegate.hour
    override val minuteOfHour: Int get() = delegate.minute
    override val secondOfMinute: Int get() = delegate.second
    override val millisOfSecond: Int get() = delegate.nanosecond / DateTimeConstants.NANOS_PER_SECOND
    override val nanosOfMilli: Int get() = delegate.nanosecond.mod(DateTimeConstants.NANOS_PER_SECOND)

    constructor(
        hourOfDay: Int,
        minuteOfHour: Int,
        secondOfMinute: Int = 0,
        millisOfSecond: Int = 0,
        nanosOfMilli: Int = 0,
    ) : this(
        LocalTime(
            hour = hourOfDay,
            minute = minuteOfHour,
            second = secondOfMinute,
            nanosecond = millisOfSecond * DateTimeConstants.NANOS_PER_SECOND + nanosOfMilli,
        )
    )

    override fun atDate(date: Date): DateTime {
        return KotlinxDateTime(
            year = date.year,
            monthNumber = date.monthNumber,
            dayOfMonth = date.dayOfMonth,
            hourOfDay = hourOfDay,
            minuteOfHour = minuteOfHour,
            secondOfMinute = secondOfMinute,
            millisOfSecond = millisOfSecond,
            nanosOfMilli = nanosOfMilli,
        )
    }

    override fun minus(value: Int, unit: TimeUnit): Time {
        val localDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val dateTime = LocalDateTime(localDate, delegate)
            .toInstant(TimeZone.currentSystemDefault())
            .minus(value, unit.fromDomain())
            .toLocalDateTime(TimeZone.currentSystemDefault()).time
        return KotlinxTime(dateTime)
    }

    override fun plus(value: Int, unit: TimeUnit): Time {
        val localDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val dateTime = LocalDateTime(localDate, delegate)
            .toInstant(TimeZone.currentSystemDefault())
            .plus(value, unit.fromDomain())
            .toLocalDateTime(TimeZone.currentSystemDefault()).time
        return KotlinxTime(dateTime)
    }

    override fun copy(
        hourOfDay: Int,
        minuteOfHour: Int,
        secondOfMinute: Int,
        millisOfSecond: Int,
        nanosOfMilli: Int,
    ): Time {
        return KotlinxTime(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond, nanosOfMilli)
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
        return delegate.toString()
    }

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