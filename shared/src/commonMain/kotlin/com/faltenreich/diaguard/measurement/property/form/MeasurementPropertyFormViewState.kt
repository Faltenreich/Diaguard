package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType

sealed class MeasurementPropertyFormViewState(val property: MeasurementProperty) {

    class Loading(
        property: MeasurementProperty,
    ) : MeasurementPropertyFormViewState(property = property)

    class Loaded(
        property: MeasurementProperty,
        val types: List<MeasurementType>,
    ) : MeasurementPropertyFormViewState(property = property)
}