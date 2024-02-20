package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

/**
 * Entity representing a default [MeasurementValue] without connection to an [Entry]
 */
data class IntermediateValue(
    val value: Double,
    val unit: MeasurementUnit,
)