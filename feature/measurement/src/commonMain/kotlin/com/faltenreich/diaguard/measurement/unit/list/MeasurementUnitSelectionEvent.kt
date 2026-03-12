package com.faltenreich.diaguard.measurement.unit.list

import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnit

sealed interface MeasurementUnitSelectionEvent {

    data class Select(val unit: MeasurementUnit.Local) : MeasurementUnitSelectionEvent
}