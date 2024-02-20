package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.measurement.type.MeasurementType

data class MeasurementTypeInputState(
    val type: MeasurementType,
    val input: String,
    val error: String? = "Error",
    val isLast: Boolean,
)