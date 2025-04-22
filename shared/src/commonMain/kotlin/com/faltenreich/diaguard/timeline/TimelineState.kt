package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.preference.color.ColorScheme

data class TimelineState(
    val initialDate: Date,
    val currentDate: Date,
    val currentDateLabel: String,
    val dateDialog: DateDialog?,
    val data: TimelineData,
    val colorScheme: ColorScheme?,
) {

    data class DateDialog(val date: Date)
}