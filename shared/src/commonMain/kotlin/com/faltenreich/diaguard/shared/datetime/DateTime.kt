package com.faltenreich.diaguard.shared.datetime

/**
 * Temporal interface representing local date and time
 */
interface DateTime : Date, Time {

    /**
     * Local date
     */
    val date: Date

    /**
     * Local time
     */
    val time: Time
}