package com.faltenreich.diaguard.datetime

import com.faltenreich.diaguard.datetime.format.DateFormatStyle

interface DateTimePlatformApi {

    fun formatDate(date: Date, style: DateFormatStyle): String

    fun getStartOfWeek(): DayOfWeek

    fun weekOfYear(date: Date): WeekOfYear

    fun is24HourFormat(): Boolean
}