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
import com.faltenreich.diaguard.shared.view.drawText
import kotlin.math.ceil

private const val Y_MIN = 0
private const val Y_MAX = 250
private const val X_MIN = 0
private const val X_MAX = 24

@Composable
fun TimelineChart(
    modifier: Modifier = Modifier
) {
    val padding = LocalDensity.current.run { AppTheme.dimensions.padding.P_2.toPx() }
    val fontSize = LocalDensity.current.run { AppTheme.typography.bodyMedium.fontSize.toPx() }
    val paint = Paint().apply {
        color = Color.Black
    }
    var offset by remember { mutableStateOf(Offset.Zero) }
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(key1 = Unit) {
                detectDragGestures(onDrag = { _, dragAmount -> offset += dragAmount })
            },
    ) {
        drawYAxis(fontSize, paint, padding)
        drawXAxis(offset, fontSize, paint, padding)
        drawValues(offset, fontSize, paint)
    }
}

private fun DrawScope.drawYAxis(
    fontSize: Float,
    paint: Paint,
    padding: Float,
) {
    val values = 0 .. 250 step 50
    val valuesCount = values.last / values.step
    values.drop(1).dropLast(1).forEach { value ->
        val index = value / values.step
        val height = size.height / valuesCount
        val x = 0f + padding
        val y = size.height - index * height
        drawText(value.toString(), x, y, fontSize, paint)
    }
}

private fun DrawScope.drawXAxis(
    offset: Offset,
    fontSize: Float,
    paint: Paint,
    padding: Float,
) {
    val hours = 0 .. 24 step 2
    val hoursCount = hours.last / hours.step

    val widthPerDay = size.width
    val widthPerHour = (widthPerDay / hoursCount).toInt()

    val xOfFirstHour = ((offset.x % widthPerHour) + padding).toInt()
    val xOfLastHour = xOfFirstHour + (hoursCount * widthPerHour)
    val y = size.height - padding
    (xOfFirstHour - widthPerHour .. xOfLastHour + widthPerHour step widthPerHour).forEach { xOfHour ->
        val xOffsetNormalized = ceil(offset.x * -1) + xOfHour
        val xOffsetInHours = xOffsetNormalized / widthPerHour
        val hourNormalized = xOffsetInHours % hoursCount
        val hour = when {
            hourNormalized >= 0 -> hourNormalized * hours.step
            else -> 24 + (hourNormalized * hours.step)
        }.toInt()
        val x = xOfHour.toFloat()
        drawText(hour.toString(), x, y, fontSize, paint)
    }
}

private fun DrawScope.drawValues(
    offset: Offset,
    fontSize: Float,
    paint: Paint,
) {
    drawText("$offset", x = size.width / 2 - 160, y = size.height / 2, fontSize, paint)
}