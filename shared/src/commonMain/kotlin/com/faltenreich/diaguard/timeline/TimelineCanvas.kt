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
import androidx.compose.ui.geometry.Size
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
    val config = TimelineConfig(
        initialDate = initialDate,
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
        val origin = Offset.Zero
        val size = size

        val dateTimeSize = Size(
            width = size.width,
            height = config.fontSize * 2 + config.padding * 3,
        )
        val listItemHeight = config.fontSize + config.padding * 2
        val listSize = Size(
            width = size.width,
            height = listItemHeight * propertiesForList.size,
        )
        val chartSize = Size(
            width = size.width,
            height = size.height - listSize.height - dateTimeSize.height,
        )

        val chartOrigin = origin
        val listOrigin = Offset(
            x = origin.x,
            y =  origin.y + chartSize.height,
        )
        val dateTimeOrigin = Offset(
            x = origin.x,
            y = origin.y + chartSize.height + listSize.height,
        )

        TimelineYAxis(
            origin = chartOrigin,
            size = chartSize,
            config = config,
        )
        TimelineList(
            origin = listOrigin,
            size = listSize,
            config = config,
            properties = propertiesForList,
        )
        TimelineXAxis(
            origin = origin,
            size = size,
            dateTimeOrigin = dateTimeOrigin,
            dateTimeSize = dateTimeSize,
            offset = offset,
            config = config,
        )
        TimelineChart(
            origin = chartOrigin,
            size = chartSize,
            offset = offset,
            config = config,
            values = valuesForChart,
        )
    }
}