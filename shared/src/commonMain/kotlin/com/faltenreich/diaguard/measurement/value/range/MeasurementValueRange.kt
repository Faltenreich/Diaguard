package com.faltenreich.diaguard.measurement.value.range

data class MeasurementValueRange(
    val minimum: Double,
    val low: Double?,
    val target: Double?,
    val high: Double?,
    val maximum: Double,
    val isHighlighted: Boolean,
)