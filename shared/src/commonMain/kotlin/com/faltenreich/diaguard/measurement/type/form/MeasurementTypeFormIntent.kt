package com.faltenreich.diaguard.measurement.type.form

sealed interface MeasurementTypeFormIntent {

    data object UpdateType : MeasurementTypeFormIntent

    data object DeleteType : MeasurementTypeFormIntent
}