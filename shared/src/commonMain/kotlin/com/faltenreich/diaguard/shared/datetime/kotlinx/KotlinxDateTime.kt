package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.datetime.DateTimeable
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.serialization.ObjectInputStream
import com.faltenreich.diaguard.shared.serialization.ObjectOutputStream
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class KotlinxDateTime(
    year: Int,
    monthOfYear: Int,
    dayOfMonth: Int,
    hourOfDay: Int,
    minuteOfHour: Int,
    secondOfMinute: Int,
    millisOfSecond: Int,
    nanosOfMillis: Int,
) : DateTimeable {

    private var localDateTime = LocalDateTime(
        year = year,
        monthNumber = monthOfYear,
        dayOfMonth = dayOfMonth,
        hour = hourOfDay,
        minute = minuteOfHour,
        second = secondOfMinute,
        nanosecond = millisOfSecond * DateTimeConstants.NANOS_PER_SECOND + nanosOfMillis,
    )

    override val date: Date
        get() = Date(
            year = localDateTime.year,
            monthOfYear = localDateTime.monthNumber,
            dayOfMonth = localDateTime.dayOfMonth,
        )

    override val time: Time
        get() = Time(
            hourOfDay = localDateTime.hour,
            minuteOfHour = localDateTime.minute,
            secondOfMinute = localDateTime.second,
            millisOfSecond = localDateTime.nanosecond / DateTimeConstants.NANOS_PER_SECOND,
            nanosOfMillis = localDateTime.nanosecond.mod(DateTimeConstants.NANOS_PER_SECOND),
        )

    override val millisSince1970: Long
        get() = localDateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

    override val isoString: String
        get() = localDateTime.toString()

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

    override fun now(): DateTimeable {
        return KotlinxDateTime(
            localDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
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