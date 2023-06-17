package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.value.MeasurementValue

data class MeasurementTypeInputViewState(
    val type: MeasurementType,
    val value: MeasurementValue?,
    val input: String,
)