package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.primitive.format

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
     * Converts millis to date
     */
    fun date(millis: Long): Date

    /**
     * Creates time from hour and minute
     */
    fun time(hourOfDay: Int, minuteOfHour: Int): Time

    /**
     * Converts date time to a string in ISO-8601 format
     */
    fun dateTimeToIsoString(dateTime: DateTime): String

    /**
     * Converts date to a string formatted for current locale
     * TODO: Localize format which is currently unsupported by kotlinx-datetime
     */
    fun dateToLocalizedString(date: Date): String {
        return "%02d.%02d.%04d".format(
            date.dayOfMonth,
            date.monthOfYear,
            date.year,
        )
    }

    /**
     * Converts time to a string formatted for current locale
     * TODO: Localize format which is currently unsupported by kotlinx-datetime
     */
    fun timeToLocalizedString(time: Time): String {
        return "%02d:%02d".format(
            time.hourOfDay,
            time.minuteOfHour,
        )
    }

    /**
     * Converts date time to a string formatted for current locale
     * TODO: Localize format which is currently unsupported by kotlinx-datetime
     */
    fun dateTimeToLocalizedString(dateTime: DateTime): String {
        return "%s %s".format(
            dateToLocalizedString(dateTime.date),
            timeToLocalizedString(dateTime.time),
        )
    }
}