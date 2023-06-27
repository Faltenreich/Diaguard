package com.faltenreich.diaguard.timeline.chart

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.timeline.chart.drawing.TimelineValues
import com.faltenreich.diaguard.timeline.chart.drawing.TimelineXAxis
import com.faltenreich.diaguard.timeline.chart.drawing.TimelineYAxis
import kotlin.math.ceil

@Composable
fun TimelineChart(
    initialDate: Date,
    values: List<MeasurementValue>,
    modifier: Modifier = Modifier,
    onDateChange: (Date) -> Unit,
) {
    // TODO: Reset remember when initialDate changes
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = TimelineChartState(
        values = values,
        initialDate = initialDate,
        offset = offset,
        dateTimeFormatter = inject(),
        padding = LocalDensity.current.run { AppTheme.dimensions.padding.P_2.toPx() },
        fontPaint = Paint().apply { color = Color.Black },
        fontSize = LocalDensity.current.run { AppTheme.typography.bodyMedium.fontSize.toPx() },
        lineColorNormal = AppTheme.colorScheme.primary,
        lineColorLow = AppTheme.colorScheme.primary,
        lineColorHigh = AppTheme.colorScheme.error,
    )
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(key1 = Unit) {
                detectDragGestures(
                    onDrag = { _, dragAmount ->
                        // TODO: Cap y at zero
                        // TODO: Change y only if delta is larger than n to prevent accidental scroll
                        offset += dragAmount
                        val widthPerDay = size.width
                        val offsetInDays = ceil(offset.x * -1) / widthPerDay
                        val date = state.initialDate.plusDays(offsetInDays.toInt())
                        onDateChange(date)
                    },
                )
            },
    ) {
        TimelineYAxis(state)
        TimelineXAxis(state)
        TimelineValues(state)
    }
}