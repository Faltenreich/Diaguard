package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.Date

data class TimelineChartState(
    val offset: Offset,
    val initialDate: Date,
    val currentDate: Date,
    val valuesForChart: List<MeasurementValue>,
    val propertiesForList: List<MeasurementProperty>,

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