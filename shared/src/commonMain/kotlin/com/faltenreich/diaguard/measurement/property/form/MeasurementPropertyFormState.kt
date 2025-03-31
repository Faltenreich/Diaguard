package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

data class MeasurementPropertyFormState(
    val property: MeasurementProperty.Local,
    val units: List<Unit>,
) {

    data class Unit(
        val unit: MeasurementUnit.Local,
        val title: String,
        val subtitle: String?,
        val isSelected: Boolean,
    )
}