package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty

data class MeasurementPropertyFormState(
    val property: MeasurementProperty,
    val unitName: String,
)