package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType

sealed class MeasurementTypeFormViewState(val type: MeasurementType) {

    class Loading(type: MeasurementType) : MeasurementTypeFormViewState(type)

    class Loaded(type: MeasurementType) : MeasurementTypeFormViewState(type)
}