package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.measurement.property.MeasurementProperty

data class MeasurementPropertyInputViewState(
    val property: MeasurementProperty,
    val values: List<MeasurementTypeInputViewState>,
)