package com.faltenreich.diaguard.datetime

class DateTimePlatformFakeApi : DateTimePlatformApi {

    override fun formatDate(date: Date): String = date.toString()

    override fun getStartOfWeek(): DayOfWeek = DayOfWeek.MONDAY

    override fun weekOfYear(date: Date): WeekOfYear = WeekOfYear(weekNumber = 1, year = 1970)

    override fun is24HourFormat(): Boolean = true
}