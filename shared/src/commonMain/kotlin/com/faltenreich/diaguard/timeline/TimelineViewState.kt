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

    // TODO: Calculate via fontSize and padding of TimelineConfig
    val dateTimeSize: Size = Size(width = timelineSize.width, height = 200f)

    // TODO: Calculate via fontSize and padding of TimelineConfig
    val listItemHeight: Float = 100f
    val listSize: Size
        get() = Size(
            width = timelineSize.width,
            height = listItemHeight * propertiesForList.size,
        )
    val listOrigin: Offset
        get() = Offset(x = 0f, y = chartSize.height)

    val chartSize: Size
        get() = Size(
            width = timelineSize.width,
            height = timelineSize.height - listSize.height - dateTimeSize.height,
        )
    val chartOrigin: Offset
        get() = timelineOrigin
}