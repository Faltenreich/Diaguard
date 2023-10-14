package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.serialization.ObjectInputStream
import com.faltenreich.diaguard.shared.serialization.ObjectOutputStream
import com.faltenreich.diaguard.shared.serialization.Serializable

interface Date : Serializable, Comparable<Date> {

    /**
     * Year starting at 0 AD
     */
    val year: Int

    /**
     * Month-of-year as number ranging from 1 to 12
     */
    val monthNumber: Int

    /**
     * Month ranging from January to December
     */
    val month: Month
        get() = Month.fromMonthNumber(monthNumber)

    /**
     * Month-of-year as pair of Month and year
     */
    val monthOfYear: MonthOfYear
        get() = MonthOfYear(month, year)

    /**
     * Day-of-month ranging from 1 to 31, depending on month
     */
    val dayOfMonth: Int

    /**
     * Day-of-week ranging from start- to end of week, depending on the locale
     */
    val dayOfWeek: DayOfWeek

    fun atTime(time: Time): DateTime {
        return DateTime(
            year = year,
            monthNumber = monthNumber,
            dayOfMonth = dayOfMonth,
            hourOfDay = time.hourOfDay,
            minuteOfHour = time.minuteOfHour,
            secondOfMinute = time.secondOfMinute,
            millisOfSecond = time.millisOfSecond,
            nanosOfMilli = time.nanosOfMilli,
        )
    }

    fun atStartOfDay(): DateTime {
        return atTime(
            time = Time(
                hourOfDay = 0,
                minuteOfHour = 0,
                secondOfMinute = 0,
                millisOfSecond = 0,
                nanosOfMilli = 0,
            )
        )
    }

    fun atEndOfDay(): DateTime {
        return atTime(
            time = Time(
                hourOfDay = 23,
                minuteOfHour = 59,
                secondOfMinute = 59,
                millisOfSecond = 999,
                nanosOfMilli = 999,
            )
        )
    }

    /**
     * Returns this date minus the given days
     */
    fun minusDays(days: Int): Date

    /**
     * Returns this date plus the given days
     */
    fun plusDays(days: Int): Date

    /**
     * Returns this date minus the given months
     */
    fun minusMonths(months: Int): Date

    /**
     * Returns this date plus the given months
     */
    fun plusMonths(months: Int): Date

    /**
     * Deserializes date
     */
    fun readObject(inputStream: ObjectInputStream)

    /**
     * Serializes date
     */
    fun writeObject(outputStream: ObjectOutputStream)
}