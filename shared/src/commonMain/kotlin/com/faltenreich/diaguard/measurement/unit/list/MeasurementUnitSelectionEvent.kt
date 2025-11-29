package com.faltenreich.diaguard.measurement.unit.list

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

sealed interface MeasurementUnitSelectionEvent {

    data class Select(val unit: MeasurementUnit.Local) : MeasurementUnitSelectionEvent
}