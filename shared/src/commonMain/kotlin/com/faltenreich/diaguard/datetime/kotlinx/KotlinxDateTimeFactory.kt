package com.faltenreich.diaguard.datetime.kotlinx

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.Time
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.seconds

class KotlinxDateTimeFactory : DateTimeFactory {

    override fun date(
        year: Int,
        monthNumber: Int,
        dayOfMonth: Int,
    ): Date {
        return KotlinxDate(
            year,
            monthNumber,
            dayOfMonth,
        )
    }

    override fun time(
        hourOfDay: Int,
        minuteOfHour: Int,
        secondOfMinute: Int,
        millisOfSecond: Int,
        nanosOfMilli: Int,
    ): Time {
        return KotlinxTime(
            hourOfDay,
            minuteOfHour,
            secondOfMinute,
            millisOfSecond,
            nanosOfMilli,
        )
    }

    override fun dateTime(
        year: Int,
        monthNumber: Int,
        dayOfMonth: Int,
        hourOfDay: Int,
        minuteOfHour: Int,
        secondOfMinute: Int,
        millisOfSecond: Int,
        nanosOfMilli: Int,
    ): DateTime {
        return KotlinxDateTime(
            year,
            monthNumber,
            dayOfMonth,
            hourOfDay,
            minuteOfHour,
            secondOfMinute,
            millisOfSecond,
            nanosOfMilli
        )
    }

    override fun dateTime(millis: Long): DateTime {
        return KotlinxDateTime(millis)
    }

    // TODO: Find a better way and test thoroughly
    override fun dateTimeFromEpoch(epochMillis: Long): DateTime {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        val offset = TimeZone.currentSystemDefault().offsetAt(Clock.System.now())
        val localDateTime = instant
            .minus(offset.totalSeconds.seconds)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        return KotlinxDateTime(localDateTime)
    }

    override fun dateTime(isoString: String): DateTime {
        return KotlinxDateTime(isoString)
    }

    override fun date(isoString: String): Date {
        return KotlinxDate(isoString)
    }

    override fun now(): KotlinxDateTime {
        return KotlinxDateTime.now()
    }
}