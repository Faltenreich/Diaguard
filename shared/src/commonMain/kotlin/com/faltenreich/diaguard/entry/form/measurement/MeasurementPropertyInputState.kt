package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.measurement.property.MeasurementProperty

data class MeasurementPropertyInputState(
    val property: MeasurementProperty,
    val typeInputDataList: List<MeasurementTypeInputState>,
    val error: String? = "Error",
)