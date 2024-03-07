package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.Date

data class TimelineState(
    val initialDate: Date,
    val currentDate: Date,
    val valuesForChart: List<MeasurementValue>,
    val valuesForList: List<MeasurementValue>,
    val propertiesForList: List<MeasurementProperty>,
)