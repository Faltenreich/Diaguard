package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.measurement.type.MeasurementType

data class MeasurementInput(
    val input: String,
    val type: MeasurementType,
)