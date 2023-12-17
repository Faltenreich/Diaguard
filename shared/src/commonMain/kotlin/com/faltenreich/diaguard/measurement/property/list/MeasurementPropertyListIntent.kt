package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.property.MeasurementProperty

sealed interface MeasurementPropertyListIntent {

    data object ShowFormDialog : MeasurementPropertyListIntent

    data object HideFormDialog : MeasurementPropertyListIntent

    data class DecrementSortIndex(val property: MeasurementProperty) : MeasurementPropertyListIntent

    data class IncrementSortIndex(val property: MeasurementProperty) : MeasurementPropertyListIntent

    data class CreateProperty(
        val name: String,
        val other: List<MeasurementProperty>,
    ) : MeasurementPropertyListIntent
}