package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.measurement.type.MeasurementType

data class MeasurementTypeInputData(
    val type: MeasurementType,
    val input: String,
)