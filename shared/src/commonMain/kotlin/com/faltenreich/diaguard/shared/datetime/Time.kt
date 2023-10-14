package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.serialization.ObjectInputStream
import com.faltenreich.diaguard.shared.serialization.ObjectOutputStream
import com.faltenreich.diaguard.shared.serialization.Serializable

interface Time : Serializable, Comparable<Time> {

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
    val nanosOfMilli: Int

    fun atDate(date: Date): DateTime

    //region Any

    fun copy(
        hourOfDay: Int = this.hourOfDay,
        minuteOfHour: Int = this.minuteOfHour,
        secondOfMinute: Int = this.secondOfMinute,
        millisOfSecond: Int = this.millisOfSecond,
        nanosOfMilli: Int = this.nanosOfMilli,
    ): Time

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int

    override fun toString(): String

    //endregion Any

    //region Serializable

    fun readObject(inputStream: ObjectInputStream)

    fun writeObject(outputStream: ObjectOutputStream)

    //endregion Serializable

    //region Comparable

    override fun compareTo(other: Time): Int {
        return when {
            hourOfDay > other.hourOfDay -> 1
            hourOfDay < other.hourOfDay -> -1
            minuteOfHour > other.minuteOfHour -> 1
            minuteOfHour < other.minuteOfHour -> -1
            secondOfMinute > other.secondOfMinute -> 1
            secondOfMinute < other.secondOfMinute -> -1
            millisOfSecond > other.millisOfSecond -> 1
            millisOfSecond < other.millisOfSecond -> -1
            nanosOfMilli > other.nanosOfMilli -> 1
            nanosOfMilli < other.nanosOfMilli -> -1
            else -> 0
        }
    }

    //endregion Comparable
}