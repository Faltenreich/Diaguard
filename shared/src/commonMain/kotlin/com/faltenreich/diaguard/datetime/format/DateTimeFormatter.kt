package com.faltenreich.diaguard.datetime.format

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.Month
import com.faltenreich.diaguard.datetime.MonthOfYear
import com.faltenreich.diaguard.datetime.Time

interface DateTimeFormatter {

    fun formatDateTime(dateTime: DateTime): String

    fun formatDate(date: Date): String

    fun formatDateRange(dateRange: ClosedRange<Date>): String

    fun formatWeek(date: Date): String

    fun formatMonth(month: Month, abbreviated: Boolean): String

    fun formatMonthOfYear(monthOfYear: MonthOfYear, abbreviated: Boolean): String

    fun formatQuarter(date: Date): String

    fun formatYear(date: Date): String

    fun formatDayOfMonth(date: Date): String

    fun formatDayOfWeek(date: Date, abbreviated: Boolean): String

    fun formatTime(time: Time): String

    fun formatTimePassed(start: DateTime, end: DateTime): String
}