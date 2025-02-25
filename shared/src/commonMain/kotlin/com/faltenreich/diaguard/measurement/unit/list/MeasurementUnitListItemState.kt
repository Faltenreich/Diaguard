package com.faltenreich.diaguard.measurement.unit.list

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

data class MeasurementUnitListItemState(
    val unit: MeasurementUnit.Local,
    val title: String,
    val subtitle: String?,
    val isSelected: Boolean,
)