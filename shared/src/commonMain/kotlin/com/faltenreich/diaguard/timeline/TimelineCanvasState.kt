package com.faltenreich.diaguard.timeline

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.Date

data class TimelineCanvasState(
    val offset: Offset,
    val initialDate: Date,
    val currentDate: Date,
    val valuesForChart: List<MeasurementValue>,
    val propertiesForList: List<MeasurementProperty>,
    val padding: Float,
    val fontSize: Float,
) {
    // Timeline
    val timelineOrigin: Offset
        get() = Offset.Zero
    // Gets set late
    var timelineSize: Size = Size.Zero

    // Chart
    val chartOrigin: Offset
        get() = timelineOrigin
    val chartSize: Size
        get() = Size(
            width = timelineSize.width,
            height = timelineSize.height - listSize.height - dateTimeSize.height,
        )

    // List
    val listItemHeight: Float
        get() = fontSize + padding * 2
    val listOrigin: Offset
        get() = Offset(
            x = timelineOrigin.x,
            y =  timelineOrigin.y + chartSize.height + dateTimeSize.height,
        )
    val listSize: Size
        get() = Size(
            width = timelineSize.width,
            height = listItemHeight * propertiesForList.size,
        )

    // Date and time
    val dateTimeOrigin: Offset
        get() = Offset(
            x = timelineOrigin.x,
            y = timelineOrigin.y + chartSize.height,
        )
    val dateTimeSize: Size
        get() = Size(
            width = timelineSize.width,
            height = fontSize * 2 + padding * 3,
        )
}