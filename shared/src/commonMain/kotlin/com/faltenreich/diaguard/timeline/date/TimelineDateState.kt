package com.faltenreich.diaguard.timeline.date

import com.faltenreich.diaguard.datetime.Date

data class TimelineDateState(
    val initialDate: Date,
    val currentDate: Date,
    val currentDateLocalized: String,
)