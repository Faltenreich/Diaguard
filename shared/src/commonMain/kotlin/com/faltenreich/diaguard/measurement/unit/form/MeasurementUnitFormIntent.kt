package com.faltenreich.diaguard.measurement.unit.form

sealed interface MeasurementUnitFormIntent {

    data object Close : MeasurementUnitFormIntent

    data class Submit(val name: String, val abbreviation: String) : MeasurementUnitFormIntent
}