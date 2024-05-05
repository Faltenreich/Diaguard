package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListItemState

data class MeasurementPropertyFormState(
    val property: MeasurementProperty,
    val unitName: String,
    val units: List<MeasurementUnitListItemState>,
)