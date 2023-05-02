package com.faltenreich.diaguard.shared.datetime

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
}