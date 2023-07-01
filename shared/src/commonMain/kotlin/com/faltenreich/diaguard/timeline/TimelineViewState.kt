package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.Date

data class TimelineViewState(
    val initialDate: Date,
    val currentDate: Date,
    val bloodSugarList: List<MeasurementValue>,
)