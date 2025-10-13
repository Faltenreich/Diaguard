package com.faltenreich.diaguard.datetime

class DateTimeFakeApi(private val is24HourFormat: Boolean = true) : DateTimePlatformApi {

    override fun formatDate(date: Date): String {
        return date.toString()
    }

    override fun getStartOfWeek(): DayOfWeek {
        return DayOfWeek.MONDAY
    }

    override fun is24HourFormat(): Boolean {
        return is24HourFormat
    }
}