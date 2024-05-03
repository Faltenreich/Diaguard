package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.value.MeasurementValue

data class TimelineState(
    val initialDate: Date,
    val currentDateLabel: String,
    val valuesForChart: List<MeasurementValue>,
    val valuesForList: List<MeasurementValue>,
    val categoriesForList: List<MeasurementCategory>,
)