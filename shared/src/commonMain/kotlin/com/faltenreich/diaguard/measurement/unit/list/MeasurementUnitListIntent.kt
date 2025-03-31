package com.faltenreich.diaguard.measurement.unit.list

sealed interface MeasurementUnitListIntent {

    data object Create : MeasurementUnitListIntent
}