package com.faltenreich.diaguard.measurement.unit.list

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

sealed interface MeasurementUnitListIntent {

    data object Close : MeasurementUnitListIntent

    data class OpenFormDialog(val unit: MeasurementUnit.Local? = null) : MeasurementUnitListIntent

    data object CloseFormDialog : MeasurementUnitListIntent

    data class StoreUnit(
        val unit: MeasurementUnit.Local?,
        val name: String,
        val abbreviation: String,
    ) : MeasurementUnitListIntent
}