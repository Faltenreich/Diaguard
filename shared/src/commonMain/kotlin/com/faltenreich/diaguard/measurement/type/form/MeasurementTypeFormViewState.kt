package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType

data class MeasurementTypeFormViewState(
    val type: MeasurementType,
    val unitName: String,
)