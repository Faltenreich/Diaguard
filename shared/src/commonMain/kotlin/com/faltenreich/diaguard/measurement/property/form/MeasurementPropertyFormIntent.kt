package com.faltenreich.diaguard.measurement.property.form

sealed interface MeasurementPropertyFormIntent {

    data object OpenIconPicker : MeasurementPropertyFormIntent

    data object UpdateProperty : MeasurementPropertyFormIntent

    data object ShowDeletionDialog : MeasurementPropertyFormIntent

    data object HideDeletionDialog : MeasurementPropertyFormIntent

    data object DeleteProperty : MeasurementPropertyFormIntent
}