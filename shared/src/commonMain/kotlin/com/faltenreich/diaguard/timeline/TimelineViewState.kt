package com.faltenreich.diaguard.timeline

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.Date

data class TimelineViewState(
    val offset: Offset,
    val initialDate: Date,
    val currentDate: Date,
    val valuesForChart: List<MeasurementValue>,
    val propertiesForList: List<MeasurementProperty>,
) {

    var timelineSize: Size = Size.Zero
    val timelineOrigin: Offset
        get() = Offset.Zero

    var chartSize: Size = Size.Zero
    val chartOrigin: Offset
        get() = Offset.Zero

    var listSize: Size = Size.Zero
    val listOrigin: Offset
        get() = Offset(x = 0f, y = chartSize.height)
}