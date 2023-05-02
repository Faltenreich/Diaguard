package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.datetime.Timeable
import kotlinx.datetime.LocalTime

class KotlinxTime(
    hourOfDay: Int,
    minuteOfHour: Int,
    secondOfMinute: Int,
    millisOfSecond: Int,
    nanosOfMillis: Int,
) : Timeable {

    private val localTime = LocalTime(
        hour = hourOfDay,
        minute = minuteOfHour,
        second = secondOfMinute,
        nanosecond = millisOfSecond * DateTimeConstants.NANOS_PER_SECOND + nanosOfMillis,
    )

    override val hourOfDay: Int
        get() = localTime.hour

    override val minuteOfHour: Int
        get() = localTime.minute

    override val secondOfMinute: Int
        get() = localTime.second

    override val millisOfSecond: Int
        get() = localTime.nanosecond / DateTimeConstants.NANOS_PER_SECOND

    override val nanosOfMillis: Int
        get() = localTime.nanosecond.mod(DateTimeConstants.NANOS_PER_SECOND)
}