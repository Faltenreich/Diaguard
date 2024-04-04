package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.type.MeasurementType

sealed interface MeasurementPropertyFormViewState {

    class Loaded(val types: List<MeasurementType>) : MeasurementPropertyFormViewState
}