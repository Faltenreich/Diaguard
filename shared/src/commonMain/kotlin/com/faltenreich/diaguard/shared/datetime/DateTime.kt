package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.primitive.format
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
) {

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

    private val localDateTime = LocalDateTime(
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

    fun localized(): String {
        return "%s %s".format(
            date.localized(),
            time.localized(),
        )
    }

    companion object {

        fun now(): DateTime {
            val localDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            return DateTime(localDateTime)
        }
    }
}