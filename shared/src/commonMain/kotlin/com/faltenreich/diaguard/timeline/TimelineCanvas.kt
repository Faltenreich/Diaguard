package com.faltenreich.diaguard.timeline

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTimeFormatter
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.timeline.chart.TimelineChart
import com.faltenreich.diaguard.timeline.chart.TimelineList
import com.faltenreich.diaguard.timeline.chart.TimelineXAxis
import com.faltenreich.diaguard.timeline.chart.TimelineYAxis
import kotlin.math.ceil

@Composable
fun TimelineCanvas(
    initialDate: Date,
    currentDate: Date,
    valuesForChart: List<MeasurementValue>,
    propertiesForList: List<MeasurementProperty>,
    onDateChange: (Date) -> Unit,
    modifier: Modifier = Modifier,
    dateTimeFormatter: DateTimeFormatter = inject(),
) {
    // TODO: Reset remember when initialDate changes
    var offset by remember { mutableStateOf(Offset.Zero) }
    val chartState = TimelineCanvasState(
        offset = offset,
        initialDate = initialDate,
        currentDate = currentDate,
        valuesForChart = valuesForChart,
        propertiesForList = propertiesForList,
        padding = LocalDensity.current.run { AppTheme.dimensions.padding.P_2.toPx() },
        fontSize = LocalDensity.current.run { AppTheme.typography.bodyMedium.fontSize.toPx() },
    )
    val config = TimelineConfig(
        textMeasurer = rememberTextMeasurer(),
        dateTimeFormatter = dateTimeFormatter,
        padding = LocalDensity.current.run { AppTheme.dimensions.padding.P_2.toPx() },
        fontPaint = Paint().apply { color = AppTheme.colors.material.onBackground },
        fontSize = LocalDensity.current.run { AppTheme.typography.bodyMedium.fontSize.toPx() },
        gridStrokeColor = AppTheme.colors.material.onSurfaceVariant,
        gridShadowColor = AppTheme.colors.material.scrim,
        valueColorNormal = AppTheme.colors.Green,
        valueColorLow = AppTheme.colors.Blue,
        valueColorHigh = AppTheme.colors.Red,
    )
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(key1 = Unit) {
                detectDragGestures(
                    onDrag = { _, dragAmount ->
                        offset += dragAmount * 1.5f

                        val widthPerDay = size.width
                        val offsetInDays = ceil(offset.x * -1) / widthPerDay
                        val date = initialDate.plusDays(offsetInDays.toInt())
                        onDateChange(date)
                    },
                )
            },
    ) {
        chartState.timelineSize = size
        // TODO: Date and time in the middle with chart above and list below, separately scrollable
        TimelineYAxis(chartState, config)
        TimelineList(chartState, config)
        TimelineXAxis(chartState, config)

        TimelineChart(
            values = valuesForChart,
            initialDate = initialDate,
            scrollOffset = offset,
            origin = chartState.chartOrigin,
            size = chartState.chartSize,
            config = config,
        )
    }
}