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

    fun readObject(inputStream: ObjectInputStream)

    fun writeObject(outputStream: ObjectOutputStream)

    fun copy(
        hourOfDay: Int = this.hourOfDay,
        minuteOfHour: Int = this.minuteOfHour,
        secondOfMinute: Int = this.secondOfMinute,
        millisOfSecond: Int = this.millisOfSecond,
        nanosOfMilli: Int = this.nanosOfMilli,
    ): Time
}