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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.datetime.DateTimeFormatter
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.drawText
import kotlin.math.ceil

@Composable
fun TimelineChart(
    values: List<MeasurementValue>,
    modifier: Modifier = Modifier,
    onDateChange: (Date) -> Unit,
) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = TimelineChartState(
        values = values,
        offset = offset,
        padding = LocalDensity.current.run { AppTheme.dimensions.padding.P_2.toPx() },
        paint = Paint().apply { color = Color.Black },
        fontSize = LocalDensity.current.run { AppTheme.typography.bodyMedium.fontSize.toPx() },
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
                        val date = DateTime.now().date.plusDays(offsetInDays.toInt())
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
    val max = yMin + yMax
    val range = yMin .. max step yStep
    val height = size.height / (range.last / range.step)
    // TODO: Move window with offset
    range.drop(1).dropLast(1).forEach { value ->
        val index = range.indexOf(value)
        val x = 0f + padding
        val y = size.height - (index * height)
        drawText(value.toString(), x, y, fontSize, paint)
    }
}

private fun DrawScope.drawXAxis(
    state: TimelineChartState,
    dateTimeFormatter: DateTimeFormatter = inject(),
) = with(state) {

    val hours = xMin .. xMax step xStep
    val hoursCount = hours.last / hours.step

    val widthPerDay = size.width
    val widthPerHour = (widthPerDay / hoursCount).toInt()

    val xOfFirstHour = (offset.x % widthPerHour).toInt()
    val xOfLastHour = xOfFirstHour + (hoursCount * widthPerHour)
    val y = size.height - padding
    // TODO: Find sweet spot for start to display approaching hour and date
    (xOfFirstHour - widthPerHour .. xOfLastHour + widthPerHour step widthPerHour).forEach { xOfHour ->
        val xOffsetNormalized = ceil(offset.x * -1) + xOfHour
        val xOffsetInHours = xOffsetNormalized / widthPerHour
        val hour = when {
            xOffsetInHours >= 0 -> (xOffsetInHours % hoursCount) * hours.step
            else -> hours.last + (xOffsetInHours % hoursCount) * hours.step
        }.toInt().let { if (it == hours.last) hours.first else it }
        val x = xOfHour.toFloat()
        if (hour == 0) {
            val xOffsetInDays = xOffsetNormalized / widthPerDay
            val date = DateTime.now().date.plusDays(xOffsetInDays.toInt())
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

private fun DrawScope.drawValues(
    state: TimelineChartState,
) = with(state) {
    val hours = xMin .. xMax step xStep

    drawText("$offset", x = size.width / 2 - 160, y = size.height / 2, fontSize, paint)

    values.forEach {
        val dateTimeBase = Date.today().atTime(Time.atStartOfDay())
        val dateTime = it.entry.dateTime
        val widthPerDay = size.width
        val widthPerHour = widthPerDay / (hours.last / hours.step)
        val widthPerMinute = widthPerHour / DateTimeConstants.MINUTES_PER_HOUR
        val offsetInMinutes = dateTimeBase.minutesUntil(dateTime)
        val offsetOfDateTime = (offsetInMinutes / hours.step) * widthPerMinute
        val x = offset.x + offsetOfDateTime

        val value = it.value
        val percentage = (value - yMin) / (yMax - yMin)
        val y = size.height - (percentage.toFloat() * size.height)

        drawCircle(
            color = Color.Green,
            radius = 20f,
            center = Offset(x, y),
        )
    }
}