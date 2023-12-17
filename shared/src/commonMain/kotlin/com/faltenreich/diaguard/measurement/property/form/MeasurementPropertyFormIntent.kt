package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType

sealed interface MeasurementPropertyFormIntent {

    data object ShowIconPicker : MeasurementPropertyFormIntent

    data object HideIconPicker : MeasurementPropertyFormIntent

    data object ShowFormDialog : MeasurementPropertyFormIntent

    data object HideFormDialog : MeasurementPropertyFormIntent

    data class CreateType(
        val typeName: String,
        val unitName: String,
        val types: List<MeasurementType>,
        val propertyId: Long,
    ) : MeasurementPropertyFormIntent

    data object ShowDeletionDialog : MeasurementPropertyFormIntent

    data object HideDeletionDialog : MeasurementPropertyFormIntent

    data class DeleteProperty(val property: MeasurementProperty) : MeasurementPropertyFormIntent
}