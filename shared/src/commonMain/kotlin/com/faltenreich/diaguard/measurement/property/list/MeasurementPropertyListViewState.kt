package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.property.MeasurementProperty

sealed class MeasurementPropertyListViewState(
    val showFormDialog: Boolean,
) {

    class Loading(
        showFormDialog: Boolean,
    ) : MeasurementPropertyListViewState(showFormDialog = showFormDialog)

    class Loaded(
        showFormDialog: Boolean,
        val listItems: List<MeasurementProperty>,
    ) : MeasurementPropertyListViewState(showFormDialog = showFormDialog)
}