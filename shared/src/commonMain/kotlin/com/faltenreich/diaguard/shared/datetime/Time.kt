package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.di.inject
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

    fun atDate(date: Date): DateTime {
        return DateTime(
            year = date.year,
            monthNumber = date.monthNumber,
            dayOfMonth = date.dayOfMonth,
            hourOfDay = hourOfDay,
            minuteOfHour = minuteOfHour,
            secondOfMinute = secondOfMinute,
            millisOfSecond = millisOfSecond,
            nanosOfMilli = nanosOfMilli,
        )
    }

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
            hourOfDay: Int,
            minuteOfHour: Int,
            secondOfMinute: Int = 0,
            millisOfSecond: Int = 0,
            nanosOfMilli: Int = 0,
        ): Time {
            return inject<DateTimeFactory>().time(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond, nanosOfMilli)
        }

        fun now(): Time {
            return DateTime.now().time
        }

        fun atStartOfDay(): Time {
            return Time(
                hourOfDay = 0,
                minuteOfHour = 0,
                secondOfMinute = 0,
                millisOfSecond = 0,
                nanosOfMilli = 0,
            )
        }

        fun atEndOfDay(): Time {
            return Time(
                hourOfDay = 23,
                minuteOfHour = 59,
                secondOfMinute = 59,
                millisOfSecond = 999,
                nanosOfMilli = 999,
            )
        }
    }
}