package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.DateTimeApi
import com.faltenreich.diaguard.shared.datetime.Time
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class KotlinxDateTimeApi : DateTimeApi {

    override fun now(): DateTime {
        return KotlinxDateTime(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
    }

    override fun isoStringToDateTime(isoString: String): DateTime {
        return KotlinxDateTime(LocalDateTime.parse(isoString))
    }

    override fun date(millis: Long): Date {
        val instant = Instant.fromEpochMilliseconds(millis)
        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return KotlinxDate(localDate)
    }

    override fun time(hourOfDay: Int, minuteOfHour: Int): Time {
        return KotlinxTime(LocalTime(hour = hourOfDay, minute = minuteOfHour))
    }

    override fun dateTimeToIsoString(dateTime: DateTime): String {
        return dateTime.toString()
    }
}