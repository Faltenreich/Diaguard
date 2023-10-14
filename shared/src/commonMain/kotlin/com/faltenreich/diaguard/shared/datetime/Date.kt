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

    fun atTime(time: Time): DateTime

    fun atStartOfDay(): DateTime

    fun atEndOfDay(): DateTime

    fun minusDays(days: Int): Date

    fun plusDays(days: Int): Date

    fun minusMonths(months: Int): Date

    fun plusMonths(months: Int): Date

    fun readObject(inputStream: ObjectInputStream)

    fun writeObject(outputStream: ObjectOutputStream)

    fun copy(
        year: Int = this.year,
        monthNumber: Int = this.monthNumber,
        dayOfMonth: Int = this.dayOfMonth,
    ): Date
}