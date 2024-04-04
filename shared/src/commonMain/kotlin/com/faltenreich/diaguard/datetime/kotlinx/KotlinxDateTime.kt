package com.faltenreich.diaguard.datetime.kotlinx

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.Time
import com.faltenreich.diaguard.datetime.factory.DateTimeConstants
import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.serialization.ObjectInputStream
import com.faltenreich.diaguard.shared.serialization.ObjectOutputStream
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until

class KotlinxDateTime(
    year: Int,
    monthNumber: Int,
    dayOfMonth: Int,
    hourOfDay: Int,
    minuteOfHour: Int,
    secondOfMinute: Int,
    millisOfSecond: Int,
    nanosOfMilli: Int,
) : DateTime {

    private var localDateTime = LocalDateTime(
        year = year,
        monthNumber = monthNumber,
        dayOfMonth = dayOfMonth,
        hour = hourOfDay,
        minute = minuteOfHour,
        second = secondOfMinute,
        nanosecond = millisOfSecond * DateTimeConstants.NANOS_PER_SECOND + nanosOfMilli,
    )

    override val date: Date
        get() = KotlinxDate(
            year = localDateTime.year,
            monthNumber = localDateTime.monthNumber,
            dayOfMonth = localDateTime.dayOfMonth,
        )

    override val time: Time
        get() = KotlinxTime(
            hourOfDay = localDateTime.hour,
            minuteOfHour = localDateTime.minute,
            secondOfMinute = localDateTime.second,
            millisOfSecond = localDateTime.nanosecond / DateTimeConstants.NANOS_PER_SECOND,
            nanosOfMilli = localDateTime.nanosecond.mod(DateTimeConstants.NANOS_PER_SECOND),
        )

    override val millisSince1970: Long
        get() = localDateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

    override val epochMilliseconds: Long
        get() = localDateTime.toInstant(TimeZone.UTC).toEpochMilliseconds()

    override val isoString: String
        get() = localDateTime.toString()

    private constructor(localDateTime: LocalDateTime) : this(
        year = localDateTime.year,
        monthNumber = localDateTime.monthNumber,
        dayOfMonth = localDateTime.dayOfMonth,
        hourOfDay = localDateTime.hour,
        minuteOfHour = localDateTime.minute,
        secondOfMinute = localDateTime.second,
        millisOfSecond = localDateTime.nanosecond / DateTimeConstants.NANOS_PER_SECOND,
        nanosOfMilli = localDateTime.nanosecond.mod(DateTimeConstants.NANOS_PER_SECOND),
    )

    constructor(isoString: String) : this(
        localDateTime = LocalDateTime.parse(isoString),
    )

    constructor(millis: Long) : this(
        localDateTime = Instant
            .fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.currentSystemDefault()),
    )

    override fun minutesUntil(other: DateTime): Long {
        val timeZone = TimeZone.currentSystemDefault()
        val instant = localDateTime.toInstant(timeZone)
        val otherLocalDateTime = KotlinxDateTime(other.millisSince1970).localDateTime
        val otherInstant = otherLocalDateTime.toInstant(timeZone)
        return instant.until(otherInstant, DateTimeUnit.MINUTE)
    }

    override fun copy(
        year: Int,
        monthNumber: Int,
        dayOfMonth: Int,
        hourOfDay: Int,
        minuteOfHour: Int,
        secondOfMinute: Int,
        millisOfSecond: Int,
        nanosOfMilli: Int
    ): DateTime {
        return KotlinxDateTime(
            year = year,
            monthNumber = monthNumber,
            dayOfMonth = dayOfMonth,
            hourOfDay = hourOfDay,
            minuteOfHour = minuteOfHour,
            secondOfMinute = secondOfMinute,
            millisOfSecond = millisOfSecond,
            nanosOfMilli = nanosOfMilli,
        )
    }

    override fun equals(other: Any?): Boolean {
        return other is DateTime &&
            date == other.date &&
            time == other.time
    }

    override fun hashCode(): Int {
        return date.hashCode() + time.hashCode()
    }

    override fun toString(): String {
        return "%s %s".format(
            date.toString(),
            time.toString(),
        )
    }

    override fun readObject(inputStream: ObjectInputStream) {
        localDateTime = Instant
            .fromEpochMilliseconds(inputStream.readLong())
            .toLocalDateTime(TimeZone.currentSystemDefault())
    }

    override fun writeObject(outputStream: ObjectOutputStream) {
        outputStream.writeLong(millisSince1970)
    }

    companion object {

        fun now(): KotlinxDateTime {
            val clock = Clock.System.now()
            val localDateTime = clock.toLocalDateTime(TimeZone.currentSystemDefault())
            return KotlinxDateTime(localDateTime)
        }
    }
}