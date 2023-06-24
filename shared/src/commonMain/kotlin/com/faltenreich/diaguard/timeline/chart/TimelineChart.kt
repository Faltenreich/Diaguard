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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.drawText
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
        paint = Paint().apply { color = Color.Black },
        fontSize = LocalDensity.current.run { AppTheme.typography.bodyMedium.fontSize.toPx() },
        lineColorNormal = AppTheme.colorScheme.primary,
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
        drawYAxis(state)
        drawXAxis(state)
        drawValues(state)
    }
}

private fun DrawScope.drawYAxis(state: TimelineChartState) = with(state) {
    val height = size.height / (yAxis.last / yAxis.step)
    // TODO: Move window with offset
    yAxis.drop(1).dropLast(1).forEach { value ->
        val index = yAxis.indexOf(value)
        val x = 0f + padding
        val y = size.height - (index * height)
        drawText(value.toString(), x, y, fontSize, paint)
    }
}

private fun DrawScope.drawXAxis(state: TimelineChartState) = with(state) {
    val y = size.height - padding

    val widthPerDay = size.width
    val widthPerHour = (widthPerDay / xAxisLabelCount).toInt()

    val xOffset = offset.x.toInt()
    val xOfFirstHour = xOffset % widthPerHour
    val xOfLastHour = xOfFirstHour + (xAxisLabelCount * widthPerHour)
    // Paint one additional hour per side to support cut-off labels
    val (xStart, xEnd) = xOfFirstHour - widthPerHour to xOfLastHour + widthPerHour
    val xProgression = xStart .. xEnd step widthPerHour

    xProgression.forEach { xOfLabel ->
        val xAbsolute = -(xOffset - xOfLabel)
        val xOffsetInHours = xAbsolute / widthPerHour
        val xOffsetInHoursOfDay = ((xOffsetInHours % xAxis.last) * xAxis.step) % xAxis.last
        val hour = when {
            xOffsetInHoursOfDay < 0 -> xOffsetInHoursOfDay + xAxis.last
            else -> xOffsetInHoursOfDay
        }

        val x = xOfLabel.toFloat()
        if (hour == 0) {
            val xOffsetInDays = xAbsolute / widthPerDay
            val date = initialDate.plusDays(xOffsetInDays.toInt())
            drawText(dateTimeFormatter.formatDate(date), x + padding, y - fontSize - padding, fontSize, paint)
            // Hide day dividers initially
            if (offset.x != 0f) {
                drawLine(
                    color = Color.Gray,
                    start = Offset(x = x, y = 0f),
                    end = Offset(x = x, y = size.height),
                    strokeWidth = strokeWidth,
                )
            }
        }
        drawText(hour.toString(), x + padding, y, fontSize, paint)
    }
}

private fun DrawScope.drawValues(state: TimelineChartState) = with(state) {
    drawText("$offset", x = padding, y = padding, fontSize, paint)

    // TODO: Get percentages from extremas
    val brush = Brush.verticalGradient(
        colorStops = arrayOf(
            .3f to lineColorHigh,
            .35f to lineColorNormal,
            .8f to lineColorNormal,
            .85f to lineColorLow,
        ),
    )

    values
        .map { value ->
            val dateTimeBase = initialDate.atTime(Time.atStartOfDay())
            val dateTime = value.entry.dateTime
            val widthPerDay = size.width
            val widthPerHour = widthPerDay / (xAxis.last / xAxis.step)
            val widthPerMinute = widthPerHour / DateTimeConstants.MINUTES_PER_HOUR
            val offsetInMinutes = dateTimeBase.minutesUntil(dateTime)
            val offsetOfDateTime = (offsetInMinutes / xAxis.step) * widthPerMinute
            val x = offset.x + offsetOfDateTime

            val percentage = (value.value - yAxis.first) / (yAxis.last - yAxis.first)
            val y = size.height - (percentage.toFloat() * size.height)

            Offset(x, y)
        }
        .zipWithNext { first, second ->
            drawLine(
                brush = brush,
                start = first,
                end = second,
                strokeWidth = strokeWidth,
            )
    }
}