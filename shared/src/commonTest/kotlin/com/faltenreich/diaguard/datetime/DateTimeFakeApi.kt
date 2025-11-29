package com.faltenreich.diaguard.datetime

class DateTimeFakeApi(private val is24HourFormat: Boolean = true) : DateTimePlatformApi {

    override fun formatDate(date: Date): String {
        return date.toString()
    }

    override fun getStartOfWeek(): DayOfWeek {
        return DayOfWeek.MONDAY
    }

    override fun weekOfYear(date: Date): WeekOfYear {
        return WeekOfYear(weekNumber = 1, year = date.year)
    }

    override fun is24HourFormat(): Boolean {
        return is24HourFormat
    }
}