package com.faltenreich.diaguard.measurement.property.form

sealed interface MeasurementPropertyFormIntent {

    data object UpdateProperty : MeasurementPropertyFormIntent

    data object DeleteProperty : MeasurementPropertyFormIntent
}