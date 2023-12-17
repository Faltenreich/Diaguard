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
}