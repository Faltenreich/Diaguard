package com.faltenreich.diaguard.measurement.type.list

import com.faltenreich.diaguard.measurement.type.MeasurementType

sealed class MeasurementTypeListViewState {

    data object Loading : MeasurementTypeListViewState()

    data class Loaded(val listItems: List<MeasurementType>) : MeasurementTypeListViewState()
}