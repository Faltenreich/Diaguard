package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.datetime.kotlinx.KotlinxDate
import com.faltenreich.diaguard.shared.datetime.kotlinx.KotlinxDateTime
import com.faltenreich.diaguard.shared.datetime.kotlinx.KotlinxTime
import com.faltenreich.diaguard.shared.primitive.format
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

// TODO: Extract everything related to kotlinx.datetime
class DateTime(
    year: Int,
    monthOfYear: Int,
    dayOfMonth: Int,
    hourOfDay: Int,
    minuteOfHour: Int,
    secondOfMinute: Int,
    millisOfSecond: Int,
    nanosOfMillis: Int,
) : Dateable by KotlinxDate(
    year = year,
    monthOfYear = monthOfYear,
    dayOfMonth = dayOfMonth,
), Timeable by KotlinxTime(
    hourOfDay = hourOfDay,
    minuteOfHour = minuteOfHour,
    secondOfMinute = secondOfMinute,
    millisOfSecond = millisOfSecond,
    nanosOfMillis = nanosOfMillis,
), DateTimeable by KotlinxDateTime(
    year = year,
    monthOfYear = monthOfYear,
    dayOfMonth = dayOfMonth,
    hourOfDay = hourOfDay,
    minuteOfHour = minuteOfHour,
    secondOfMinute = secondOfMinute,
    millisOfSecond = millisOfSecond,
    nanosOfMillis = nanosOfMillis,
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