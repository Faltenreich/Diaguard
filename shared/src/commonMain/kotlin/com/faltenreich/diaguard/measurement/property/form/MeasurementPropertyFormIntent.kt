package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRangeState

sealed interface MeasurementPropertyFormIntent {

    data class UpdateProperty(val name: String) : MeasurementPropertyFormIntent

    data class UpdateValueRange(val valueRange: MeasurementValueRangeState) : MeasurementPropertyFormIntent

    data object OpenUnitSearch : MeasurementPropertyFormIntent

    data class SelectUnit(val unit: MeasurementUnit.Local) : MeasurementPropertyFormIntent

    data object Submit : MeasurementPropertyFormIntent

    data object OpenDeleteDialog : MeasurementPropertyFormIntent

    data object CloseDeleteDialog : MeasurementPropertyFormIntent

    data object OpenAlertDialog : MeasurementPropertyFormIntent

    data object CloseAlertDialog : MeasurementPropertyFormIntent

    data class Delete(val needsConfirmation: Boolean) : MeasurementPropertyFormIntent
}