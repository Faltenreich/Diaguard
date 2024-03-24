package com.faltenreich.diaguard.datetime

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
     * Localized milliseconds since 01/01/1970
     */
    val millisSince1970: Long

    /**
     * UTC milliseconds since 01/01/1970
     */
    val epochMilliseconds: Long

    /**
     * Date and time in ISO 8601 format
     */
    val isoString: String

    fun minutesUntil(other: DateTime): Long

    //region Any

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

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int

    override fun toString(): String

    //endregion Any

    //region Serializable

    fun readObject(inputStream: ObjectInputStream)

    fun writeObject(outputStream: ObjectOutputStream)

    //endregion Serializable

    //region Comparable

    override fun compareTo(other: DateTime): Int {
        return when {
            date > other.date -> 1
            date < other.date -> -1
            time > other.time -> 1
            time < other.time -> -1
            else -> 0
        }
    }

    //endregion Comparable
}