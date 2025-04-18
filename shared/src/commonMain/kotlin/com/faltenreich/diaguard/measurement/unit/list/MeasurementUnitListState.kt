package com.faltenreich.diaguard.measurement.unit.list

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

data class MeasurementUnitListState(
    val units: List<MeasurementUnit.Local>,
    val formDialog: FormDialog?,
) {

    data class FormDialog(val unit: MeasurementUnit.Local? = null)
}