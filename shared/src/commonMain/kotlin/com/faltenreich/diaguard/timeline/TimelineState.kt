package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date

data class TimelineState(
    val initialDate: Date,
    val currentDate: Date,
    val currentDateLabel: String,
    val dateDialog: DateDialog?,
    val data: TimelineData,
) {

    data class DateDialog(val date: Date)
}