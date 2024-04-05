package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType

sealed interface MeasurementTypeFormViewState {

    data class Loaded(
        val type: MeasurementType,
        val unitName: String,
    ) : MeasurementTypeFormViewState

    data object Error : MeasurementTypeFormViewState
}