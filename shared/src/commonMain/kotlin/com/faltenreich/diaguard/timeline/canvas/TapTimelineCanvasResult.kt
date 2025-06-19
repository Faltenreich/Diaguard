package com.faltenreich.diaguard.timeline.canvas

import com.faltenreich.diaguard.measurement.value.MeasurementValue

sealed interface TapTimelineCanvasResult {

    data class Chart(val value: MeasurementValue.Local): TapTimelineCanvasResult

    data class Table(val values: List<MeasurementValue.Local>): TapTimelineCanvasResult

    data object None : TapTimelineCanvasResult
}