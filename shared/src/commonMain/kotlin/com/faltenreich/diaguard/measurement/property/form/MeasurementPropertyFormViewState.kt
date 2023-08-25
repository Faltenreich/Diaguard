package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType

sealed class MeasurementPropertyFormViewState(
    val property: MeasurementProperty,
    val showFormDialog: Boolean,
) {

    class Loading(
        property: MeasurementProperty,
        showFormDialog: Boolean,
    ) : MeasurementPropertyFormViewState(
        property = property,
        showFormDialog = showFormDialog,
    )

    class Loaded(
        property: MeasurementProperty,
        showFormDialog: Boolean,
        val types: List<MeasurementType>,
    ) : MeasurementPropertyFormViewState(
        property = property,
        showFormDialog = showFormDialog,
    )
}