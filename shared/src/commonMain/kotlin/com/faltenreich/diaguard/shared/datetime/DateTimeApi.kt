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
        // TODO: Localize format which is currently unsupported by kotlinx-datetime
        return dateTime.run {
            "%02d.%02d.%04d %02d:%02d".format(
                dayOfMonth,
                monthOfYear,
                year,
                hourOfDay,
                minuteOfHour,
            )
        }
    }
}