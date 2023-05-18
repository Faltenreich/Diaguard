package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType

data class MeasurementInputViewState(
    val properties: List<Property>,
) {

    data class Property(
        val property: MeasurementProperty,
        val values: List<Value>,
    ) {

        data class Value(
            val type: MeasurementType,
        )
    }
}