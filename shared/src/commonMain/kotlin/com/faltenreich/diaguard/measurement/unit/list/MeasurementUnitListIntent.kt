package com.faltenreich.diaguard.measurement.unit.list

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

sealed interface MeasurementUnitListIntent {

    data object Create : MeasurementUnitListIntent

    data class Edit(val unit: MeasurementUnit.Local) : MeasurementUnitListIntent
}