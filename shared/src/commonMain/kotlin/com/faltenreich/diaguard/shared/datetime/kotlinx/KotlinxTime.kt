package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.Time
import kotlinx.datetime.LocalTime

class KotlinxTime(private val localTime: LocalTime) : Time {

    override val hourOfDay: Int
        get() = localTime.hour

    override val minuteOfHour: Int
        get() = localTime.minute

    override val secondOfMinute: Int
        get() = localTime.second

    override val millisOfSecond: Int
        get() = localTime.nanosecond / NANO_SECONDS_PER_SECOND

    override val nanosOfMillis: Int
        get() = localTime.nanosecond.mod(NANO_SECONDS_PER_SECOND)

    override fun toString(): String {
        return localTime.toString()
    }

    companion object {

        private const val NANO_SECONDS_PER_SECOND = 1_000
    }
}