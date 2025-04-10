package com.faltenreich.diaguard.measurement.unit.form

sealed interface MeasurementUnitFormIntent {

    data object Close : MeasurementUnitFormIntent

    data object Submit : MeasurementUnitFormIntent
}