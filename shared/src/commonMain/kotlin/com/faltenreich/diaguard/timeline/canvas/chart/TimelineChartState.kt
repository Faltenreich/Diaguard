package com.faltenreich.diaguard.timeline.canvas.chart

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.property.MeasurementProperty

data class TimelineChartState(
    val initialDateTime: DateTime,
    val property: MeasurementProperty,
    val values: List<Value>,
) {

    data class Value(
        val dateTime: DateTime,
        val value: Double,
    )
}