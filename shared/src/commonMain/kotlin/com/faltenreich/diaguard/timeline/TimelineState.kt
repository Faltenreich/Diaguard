package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvasState
import com.faltenreich.diaguard.timeline.date.TimelineDateState

data class TimelineState(
    val date: TimelineDateState,
    val canvas: TimelineCanvasState?,
    val valueBottomSheet: ValueBottomSheet?,
) {

    data class ValueBottomSheet(val values: List<MeasurementValue.Local>)
}