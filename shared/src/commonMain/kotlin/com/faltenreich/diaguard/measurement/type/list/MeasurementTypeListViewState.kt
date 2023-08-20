package com.faltenreich.diaguard.measurement.type.list

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType

sealed class MeasurementTypeListViewState(val property: MeasurementProperty) {

    class Loading(
        property: MeasurementProperty,
    ) : MeasurementTypeListViewState(property = property)

    class Loaded(
        property: MeasurementProperty,
        val listItems: List<MeasurementType>,
    ) : MeasurementTypeListViewState(property = property)
}