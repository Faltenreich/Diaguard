package com.faltenreich.diaguard.shared.datetime

interface DateTimeApi {

    /**
     * Returns the current point in time
     */
    fun now(): DateTime

    /**
     * Converts string in ISO-8601 format to date time
     */
    fun isoStringToDateTime(isoString: String): DateTime

    /**
     * Converts date time to a string in ISO-8601 format
     */
    fun dateTimeToIsoString(dateTime: DateTime): String

    /**
     * Converts date time to a string formatted for current locale
     */
    fun dateTimeToLocalizedString(dateTime: DateTime): String {
        TODO("Unsupported by kotlinx-datetime")
    }
}