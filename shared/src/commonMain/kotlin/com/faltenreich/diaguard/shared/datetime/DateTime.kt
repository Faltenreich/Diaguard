package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.serialization.ObjectInputStream
import com.faltenreich.diaguard.shared.serialization.ObjectOutputStream
import com.faltenreich.diaguard.shared.serialization.Serializable

interface DateTime : Serializable, Comparable<DateTime> {

    /**
     * Date component
     */
    val date: Date

    /**
     * Time component
     */
    val time: Time

    /**
     * Milliseconds since 01/01/1970
     */
    val millisSince1970: Long

    /**
     * Date and time in ISO 8601 format
     */
    val isoString: String

    @Deprecated("")
    fun now(): DateTime

    fun minutesUntil(other: DateTime): Long

    fun readObject(inputStream: ObjectInputStream)

    fun writeObject(outputStream: ObjectOutputStream)

    fun copy(
        year: Int = date.year,
        monthNumber: Int = date.monthNumber,
        dayOfMonth: Int = date.dayOfMonth,
        hourOfDay: Int = time.hourOfDay,
        minuteOfHour: Int = time.minuteOfHour,
        secondOfMinute: Int = time.secondOfMinute,
        millisOfSecond: Int = time.millisOfSecond,
        nanosOfMilli: Int = time.nanosOfMilli,
    ): DateTime
}