package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.property.MeasurementProperty

sealed class MeasurementPropertyListViewState {

    data object Loading : MeasurementPropertyListViewState()

    data class Loaded(val listItems: List<MeasurementProperty>) : MeasurementPropertyListViewState()
}