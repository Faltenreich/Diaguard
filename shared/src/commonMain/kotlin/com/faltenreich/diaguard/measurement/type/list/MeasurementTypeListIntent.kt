package com.faltenreich.diaguard.measurement.type.list

import com.faltenreich.diaguard.measurement.type.MeasurementType

sealed interface MeasurementTypeListIntent {

    data class DecrementSortIndex(
        val type: MeasurementType,
        val inTypes: List<MeasurementType>,
    ) : MeasurementTypeListIntent

    data class IncrementSortIndex(
        val type: MeasurementType,
        val inTypes: List<MeasurementType>,
    ) : MeasurementTypeListIntent

    data class EditType(val type: MeasurementType) : MeasurementTypeListIntent

    data object ShowFormDialog : MeasurementTypeListIntent

    data object HideFormDialog : MeasurementTypeListIntent

    data class CreateType(
        val typeName: String,
        val unitName: String,
        val types: List<MeasurementType>,
        val categoryId: Long,
    ) : MeasurementTypeListIntent
}