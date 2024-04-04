package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType

sealed interface MeasurementPropertyFormViewState {

    class Loaded(
        val property: MeasurementProperty,
        val showIconPicker: Boolean,
        val showDeletionDialog: Boolean,
        val types: List<MeasurementType>,
        val measurementCount: Long,
    ) : MeasurementPropertyFormViewState
}