package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.measurement.property.MeasurementProperty

data class MeasurementPropertyInputData(
    val property: MeasurementProperty,
    val typeInputDataList: List<MeasurementTypeInputData>,
)