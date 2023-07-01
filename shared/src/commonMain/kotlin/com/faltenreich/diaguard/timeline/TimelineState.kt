package com.faltenreich.diaguard.timeline

import androidx.compose.ui.geometry.Offset
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.Date

data class TimelineState(
    val offset: Offset,
    val initialDate: Date,
    val values: List<MeasurementValue>,
)