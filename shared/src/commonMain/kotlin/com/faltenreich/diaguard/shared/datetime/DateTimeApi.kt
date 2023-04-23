package com.faltenreich.diaguard.shared.datetime

interface DateTimeApi {

    /**
     * Returns the current point in time
     */
    fun now(): DateTime

    /**
     * Converts date time to a string in ISO-8601 format
     */
    fun convertDateTimeToIsoString(dateTime: DateTime): String

    /**
     * Converts string in ISO-8601 format to date time
     */
    fun convertIsoStringToDateTime(isoString: String): DateTime
}