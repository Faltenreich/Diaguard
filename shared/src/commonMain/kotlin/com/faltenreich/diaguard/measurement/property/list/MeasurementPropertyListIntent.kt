package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.property.MeasurementProperty

sealed interface MeasurementPropertyListIntent {

    data class DecrementSortIndex(val property: MeasurementProperty) : MeasurementPropertyListIntent

    data class IncrementSortIndex(val property: MeasurementProperty) : MeasurementPropertyListIntent

    data class Edit(val property: MeasurementProperty) : MeasurementPropertyListIntent

    data object Create : MeasurementPropertyListIntent
}