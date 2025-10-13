package com.faltenreich.diaguard.datetime

interface DateTimePlatformApi {

    fun formatDate(date: Date): String

    fun getStartOfWeek(): DayOfWeek

    fun is24HourFormat(): Boolean
}