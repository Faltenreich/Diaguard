package com.faltenreich.diaguard.timeline.date

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.timeline.TimelineState.DateDialog

data class TimelineDateState(
    val initial: Date,
    val current: Date,
    val label: String,
    val pickerDialog: DateDialog?,
)