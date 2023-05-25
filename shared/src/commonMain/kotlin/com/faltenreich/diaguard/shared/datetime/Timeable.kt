package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.serialization.Serializable

interface Timeable : Serializable, Comparable<Timeable> {

    /**
     * Hour-of-day ranging from 0 to 24
     */
    val hourOfDay: Int

    /**
     * Minute-of-hour ranging from 0 to 60
     */
    val minuteOfHour: Int

    /**
     * Second-of-minute ranging from 0 to 60
     */
    val secondOfMinute: Int

    /**
     * Milliseconds-of-second ranging from 0 to 1,000
     */
    val millisOfSecond: Int

    /**
     * Nanoseconds-of-millisecond ranging from 0 to 1,000
     */
    val nanosOfMillis: Int

    override fun compareTo(other: Timeable): Int {
        return compareValuesBy(
            this,
            other,
            { it.hourOfDay },
            { it.minuteOfHour },
            { it.secondOfMinute },
            { it.millisOfSecond },
            { it.nanosOfMillis },
        )
    }
}