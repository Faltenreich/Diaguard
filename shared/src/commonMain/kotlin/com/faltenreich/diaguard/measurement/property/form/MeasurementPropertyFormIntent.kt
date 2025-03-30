package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

sealed interface MeasurementPropertyFormIntent {

    data object UpdateProperty : MeasurementPropertyFormIntent

    data object DeleteProperty : MeasurementPropertyFormIntent

    data class SelectUnit(val unit: MeasurementUnit.Local) : MeasurementPropertyFormIntent
}