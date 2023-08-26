package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.unit.MeasurementTypeUnit

sealed class MeasurementTypeFormViewState(val type: MeasurementType) {

    class Loading(type: MeasurementType) : MeasurementTypeFormViewState(type)

    class Loaded(
        type: MeasurementType,
        val typeUnits: List<MeasurementTypeUnit>,
    ) : MeasurementTypeFormViewState(type)
}