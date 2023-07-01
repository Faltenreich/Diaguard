package com.faltenreich.diaguard.timeline

import androidx.compose.ui.geometry.Offset
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.Date

data class TimelineViewState(
    val offset: Offset,
    val initialDate: Date,
    val currentDate: Date,
    val bloodSugarList: List<MeasurementValue>,
)