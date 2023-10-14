package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.di.inject
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

    /**
     * Current point in time
     */
    fun now(): DateTime

    fun minutesUntil(other: DateTime): Long

    /**
     * Deserializes date
     */
    fun readObject(inputStream: ObjectInputStream)

    /**
     * Serializes date
     */
    fun writeObject(outputStream: ObjectOutputStream)

    companion object {

        operator fun invoke(
            year: Int,
            monthNumber: Int,
            dayOfMonth: Int,
            hourOfDay: Int,
            minuteOfHour: Int,
            secondOfMinute: Int,
            millisOfSecond: Int,
            nanosOfMilli: Int,
        ): DateTime {
            return inject<DateTimeFactory>().dateTime(
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

        operator fun invoke(
            millis: Long
        ): DateTime {
            return inject<DateTimeFactory>().dateTime(millis = millis)
        }

        operator fun invoke(isoString: String): DateTime {
            return inject<DateTimeFactory>().dateTime(isoString)
        }

        fun now(): DateTime {
            return inject<DateTimeFactory>().now()
        }
    }
}