package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

/**
 * Entity representing a custom [MeasurementValue] that has been entered by the user
 */
data class InputValue(
    val value: String,
    val unit: MeasurementUnit,
)