package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType

sealed interface MeasurementTypeFormViewState {

    data class Loaded(
        val type: MeasurementType,
        val showDeletionDialog: Boolean,
        val measurementCount: Long,
    ) : MeasurementTypeFormViewState

    data object Error : MeasurementTypeFormViewState
}