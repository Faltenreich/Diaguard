package com.faltenreich.diaguard.timeline.date

import com.faltenreich.diaguard.datetime.Date

data class TimelineDateState(
    val initial: Date,
    val current: Date,
    val label: String,
)