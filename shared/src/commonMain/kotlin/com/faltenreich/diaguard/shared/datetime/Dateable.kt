package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.serialization.ObjectInputStream
import com.faltenreich.diaguard.shared.serialization.ObjectOutputStream

interface Dateable {

    /**
     * Year starting at 0 AD
     */
    val year: Int

    /**
     * Month-of-year ranging from 1 to 12
     */
    val monthOfYear: Int

    /**
     * Month ranging from January to December
     */
    val month: Month
        get() = Month.fromMonthOfYear(monthOfYear)

    /**
     * Day-of-month ranging from 1 to 31, depending on month
     */
    val dayOfMonth: Int

    /**
     * Day-of-week ranging from start- to end of week, depending on the locale
     */
    val dayOfWeek: DayOfWeek

    /**
     * Returns this date minus the given days
     */
    fun minusDays(days: Int): Dateable

    /**
     * Returns this date plus the given days
     */
    fun plusDays(days: Int): Dateable

    /**
     * Returns this date minus the given months
     */
    fun minusMonths(months: Int): Dateable

    /**
     * Returns this date plus the given months
     */
    fun plusMonths(months: Int): Dateable

    /**
     * Deserializes date
     */
    fun readObject(inputStream: ObjectInputStream)

    /**
     * Serializes date
     */
    fun writeObject(outputStream: ObjectOutputStream)
}