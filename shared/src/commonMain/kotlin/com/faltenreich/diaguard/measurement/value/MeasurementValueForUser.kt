package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

/**
 * Entity representing a [MeasurementValue] in the localized and user-selected [MeasurementUnit]
 */
data class MeasurementValueForUser(
    val value: String,
    val unit: MeasurementUnit,
)