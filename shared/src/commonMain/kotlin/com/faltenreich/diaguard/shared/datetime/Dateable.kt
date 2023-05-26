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
     * Day-of-month ranging from 1 to 31, depending on month
     */
    val dayOfMonth: Int

    /**
     * Returns this date plus the given days
     */
    fun plusDays(days: Int): Dateable

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