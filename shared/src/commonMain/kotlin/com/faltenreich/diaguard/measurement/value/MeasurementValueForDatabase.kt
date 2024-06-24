package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

/**
 * Entity representing a [MeasurementValue] in the default [MeasurementUnit]
 */
data class MeasurementValueForDatabase(
    val value: Double,
    val unit: MeasurementUnit,
)