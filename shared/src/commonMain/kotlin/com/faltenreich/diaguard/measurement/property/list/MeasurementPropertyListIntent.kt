package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
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

    data class CreateProperty(
        val category: MeasurementCategory,
        val properties: List<MeasurementProperty>,
    ) : MeasurementPropertyListIntent
}