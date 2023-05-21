package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.measurement.type.MeasurementType

data class MeasurementInput(
    val type: MeasurementType,
    val input: String,
)