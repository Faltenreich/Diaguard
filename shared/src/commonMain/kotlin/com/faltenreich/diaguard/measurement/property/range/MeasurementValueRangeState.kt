package com.faltenreich.diaguard.measurement.property.range

data class MeasurementValueRangeState(
    val minimum: String,
    val low: String,
    val target: String,
    val high: String,
    val maximum: String,
    val isHighlighted: Boolean,
    val unit: String?,
)