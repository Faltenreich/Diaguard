package com.faltenreich.diaguard.shared.datetime

import kotlin.time.Duration.Companion.seconds

@Suppress("unused", "MemberVisibilityCanBePrivate")
object DateTimeConstants {

    const val MONTHS_PER_YEAR = 12

    const val DAYS_PER_WEEK = 7

    const val HOURS_PER_DAY = 24

    const val MINUTES_PER_HOUR = 60
    const val MINUTES_PER_DAY = MINUTES_PER_HOUR * HOURS_PER_DAY

    const val SECONDS_PER_MINUTE = 60
    const val SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR
    const val SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY

    const val MILLIS_PER_SECOND = 1_000
    const val MILLIS_PER_MINUTE = MILLIS_PER_SECOND * SECONDS_PER_MINUTE
    const val MILLIS_PER_HOUR = MILLIS_PER_MINUTE * MINUTES_PER_HOUR
    const val MILLIS_PER_DAY = MILLIS_PER_HOUR * HOURS_PER_DAY

    const val NANOS_PER_MILLI = 1_000
    const val NANOS_PER_SECOND = NANOS_PER_MILLI * MILLIS_PER_SECOND
    const val NANOS_PER_MINUTE = NANOS_PER_SECOND * SECONDS_PER_MINUTE
    const val NANOS_PER_HOUR = NANOS_PER_MINUTE.toLong() * MINUTES_PER_HOUR.toLong()
    const val NANOS_PER_DAY = NANOS_PER_HOUR * HOURS_PER_DAY.toLong()

    val INPUT_DEBOUNCE = 1.seconds
}