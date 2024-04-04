package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty

sealed interface MeasurementPropertyFormIntent {

    data object ShowIconPicker : MeasurementPropertyFormIntent

    data object HideIconPicker : MeasurementPropertyFormIntent

    data object Submit : MeasurementPropertyFormIntent

    data object ShowDeletionDialog : MeasurementPropertyFormIntent

    data object HideDeletionDialog : MeasurementPropertyFormIntent

    data class DeleteProperty(val property: MeasurementProperty) : MeasurementPropertyFormIntent
}