package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.text.TextMeasurer
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.Date

data class TimelineChartState(
    val offset: Offset,
    val initialDate: Date,
    val currentDate: Date,
    val valuesForChart: List<MeasurementValue>,
    val propertiesForList: List<MeasurementProperty>,

    val textMeasurer: TextMeasurer,

    val padding: Float,
    val fontPaint: Paint,
    val fontSize: Float,

    val gridStrokeColor: Color,
    val gridStrokeWidth: Float = 0f,
    val gridShadowColor: Color,

    val valueColorNormal: Color,
    val valueColorLow: Color,
    val valueColorHigh: Color,
    val valueStrokeWidth: Float = 8f,
    val valueDotRadius: Float = 16f,

    private val xMin: Int = 0,
    private val xMax: Int = 24,
    private val xStep: Int = 2,

    private val yMin: Int = 0,
    private val yMax: Int = 250,
    private val yStep: Int = 50,
) {

    private val xRange: IntRange = xMin .. xMax
    val xAxis: IntProgression = xRange step xStep
    val xAxisLabelCount: Int = xRange.last / xAxis.step

    private val yRange: IntRange = yMin .. yMax
    val yAxis: IntProgression = yRange step yStep

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