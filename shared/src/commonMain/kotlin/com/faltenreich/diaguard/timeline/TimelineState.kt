package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.datetime.Date

data class TimelineState(
    val initialDate: Date,
    val currentDate: Date,
    val valuesForChart: List<MeasurementValue>,
    val valuesForList: List<MeasurementValue>,
    val categoriesForList: List<MeasurementCategory>,
)