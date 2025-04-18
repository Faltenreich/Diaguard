package com.faltenreich.diaguard.measurement.unit.list

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

data class MeasurementUnitListState(
    val units: List<MeasurementUnit.Local>,
    val formState: Form,
) {

    sealed interface Form {

        data object Hidden : Form

        data class Shown(val unit: MeasurementUnit.Local? = null) : Form
    }
}