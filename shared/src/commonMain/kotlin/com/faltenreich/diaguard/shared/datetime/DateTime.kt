package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.serialization.ObjectInputStream
import com.faltenreich.diaguard.shared.serialization.ObjectOutputStream
import com.faltenreich.diaguard.shared.serialization.Serializable
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class DateTime(
    year: Int,
    monthOfYear: Int,
    dayOfMonth: Int,
    hourOfDay: Int,
    minuteOfHour: Int,
    secondOfMinute: Int,
    millisOfSecond: Int,
    nanosOfMillis: Int,
) : Serializable, Comparable<DateTime> {

    private constructor(localDateTime: LocalDateTime) : this(
        year = localDateTime.year,
        monthOfYear = localDateTime.monthNumber,
        dayOfMonth = localDateTime.dayOfMonth,
        hourOfDay = localDateTime.hour,
        minuteOfHour = localDateTime.minute,
        secondOfMinute = localDateTime.second,
        millisOfSecond = localDateTime.nanosecond / DateTimeConstants.NANOS_PER_SECOND,
        nanosOfMillis = localDateTime.nanosecond.mod(DateTimeConstants.NANOS_PER_SECOND),
    )

    constructor(isoString: String) : this(
        localDateTime = LocalDateTime.parse(isoString),
    )

    constructor(millis: Long) : this(
        localDateTime = Instant
            .fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.currentSystemDefault()),
    )

    private var localDateTime = LocalDateTime(
        year = year,
        monthNumber = monthOfYear,
        dayOfMonth = dayOfMonth,
        hour = hourOfDay,
        minute = minuteOfHour,
        second = secondOfMinute,
        nanosecond = millisOfSecond * DateTimeConstants.NANOS_PER_SECOND + nanosOfMillis,
    )

    val date: Date
        get() = Date(
            year = localDateTime.year,
            monthOfYear = localDateTime.monthNumber,
            dayOfMonth = localDateTime.dayOfMonth,
        )

    val time: Time
        get() = Time(
            hourOfDay = localDateTime.hour,
            minuteOfHour = localDateTime.minute,
            secondOfMinute = localDateTime.second,
            millisOfSecond = localDateTime.nanosecond / DateTimeConstants.NANOS_PER_SECOND,
            nanosOfMillis = localDateTime.nanosecond.mod(DateTimeConstants.NANOS_PER_SECOND),
        )

    val millisSince1970: Long
        get() = localDateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

    val isoString: String
        get() = localDateTime.toString()

    override fun compareTo(other: DateTime): Int {
        return compareValuesBy(
            this,
            other,
            { it.date },
            { it.time },
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

    @Suppress("unused")
    private fun readObject(inputStream: ObjectInputStream) {
        localDateTime = Instant
            .fromEpochMilliseconds(inputStream.readLong())
            .toLocalDateTime(TimeZone.currentSystemDefault())
    }

    @Suppress("unused")
    private fun writeObject(outputStream: ObjectOutputStream) {
        outputStream.writeLong(millisSince1970)
    }

    companion object {

        fun now(): DateTime {
            val now = Clock.System.now()
            val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
            return DateTime(localDateTime)
        }
    }
}