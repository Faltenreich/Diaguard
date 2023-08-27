package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.unit.MeasurementTypeUnit

sealed class MeasurementTypeFormViewState {

    data object Loading : MeasurementTypeFormViewState()

    data class Loaded(
        val type: MeasurementType,
        val typeUnits: List<MeasurementTypeUnit>,
    ) : MeasurementTypeFormViewState()

    data object Error : MeasurementTypeFormViewState()
}