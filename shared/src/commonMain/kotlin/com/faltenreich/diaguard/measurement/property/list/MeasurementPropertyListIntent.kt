package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.property.MeasurementProperty

sealed interface MeasurementPropertyListIntent {

    data class DecrementSortIndex(
        val property: MeasurementProperty,
        val inProperties: List<MeasurementProperty>,
    ) : MeasurementPropertyListIntent

    data class IncrementSortIndex(
        val property: MeasurementProperty,
        val inProperties: List<MeasurementProperty>,
    ) : MeasurementPropertyListIntent

    data class EditProperty(val property: MeasurementProperty) : MeasurementPropertyListIntent

    data object ShowFormDialog : MeasurementPropertyListIntent

    data object HideFormDialog : MeasurementPropertyListIntent

    data class CreateProperty(
        val propertyName: String,
        val unitName: String,
        val properties: List<MeasurementProperty>,
        val categoryId: Long,
    ) : MeasurementPropertyListIntent
}