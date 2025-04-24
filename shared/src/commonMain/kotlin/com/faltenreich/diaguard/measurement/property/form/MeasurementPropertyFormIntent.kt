package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

sealed interface MeasurementPropertyFormIntent {

    data object UpdateProperty : MeasurementPropertyFormIntent

    data class Delete(val needsConfirmation: Boolean) : MeasurementPropertyFormIntent

    data object OpenDeleteDialog : MeasurementPropertyFormIntent

    data object CloseDeleteDialog : MeasurementPropertyFormIntent

    data object OpenAlertDialog : MeasurementPropertyFormIntent

    data object CloseAlertDialog : MeasurementPropertyFormIntent

    data object OpenUnitSearch : MeasurementPropertyFormIntent

    data class SelectUnit(val unit: MeasurementUnit.Local) : MeasurementPropertyFormIntent
}