package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType

sealed class MeasurementPropertyFormViewState(val property: MeasurementProperty) {

    class Loading(
        property: MeasurementProperty,
    ) : MeasurementPropertyFormViewState(property)

    class Loaded(
        property: MeasurementProperty,
        val showIconPicker: Boolean,
        val showFormDialog: Boolean,
        val showDeletionDialog: Boolean,
        val types: List<MeasurementType>,
        val measurementCount: Long,
    ) : MeasurementPropertyFormViewState(property)
}