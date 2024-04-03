package com.faltenreich.diaguard.measurement.unit.list

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

sealed interface MeasurementUnitListIntent {

    data class Select(val unit: MeasurementUnit) : MeasurementUnitListIntent
}