package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty

sealed interface MeasurementPropertyListIntent {

    data class DecrementSortIndex(
        val property: MeasurementProperty.Local,
        val inProperties: List<MeasurementProperty.Local>,
    ) : MeasurementPropertyListIntent

    data class IncrementSortIndex(
        val property: MeasurementProperty.Local,
        val inProperties: List<MeasurementProperty.Local>,
    ) : MeasurementPropertyListIntent

    data class EditProperty(val property: MeasurementProperty.Local) : MeasurementPropertyListIntent

    data class CreateProperty(
        val category: MeasurementCategory.Local,
        val properties: List<MeasurementProperty.Local>,
    ) : MeasurementPropertyListIntent
}