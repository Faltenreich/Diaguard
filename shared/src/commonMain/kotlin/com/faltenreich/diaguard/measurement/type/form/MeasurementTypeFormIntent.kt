package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType

sealed interface MeasurementTypeFormIntent {

    data object ShowDeletionDialog : MeasurementTypeFormIntent

    data object HideDeletionDialog : MeasurementTypeFormIntent

    data class DeleteType(val type: MeasurementType) : MeasurementTypeFormIntent
}