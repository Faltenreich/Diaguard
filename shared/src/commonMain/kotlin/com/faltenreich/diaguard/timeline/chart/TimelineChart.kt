package com.faltenreich.diaguard.timeline.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.view.drawText

private const val Y_MIN = 0
private const val Y_MAX = 250
private const val X_MIN = 0
private const val X_MAX = 24

@Composable
fun TimelineChart(
    modifier: Modifier = Modifier,
) {
    val offset = LocalDensity.current.run { AppTheme.dimensions.padding.P_4.toPx() }
    val fontSize = LocalDensity.current.run { AppTheme.typography.bodyMedium.fontSize.toPx() }
    Canvas(
        modifier = modifier.fillMaxSize(),
    ) {
        drawAxis(offset = offset, fontSize = fontSize)
    }
}

private fun DrawScope.drawAxis(
    offset: Float,
    fontSize: Float,
) {
    drawLine(
        color = Color.Black,
        start = Offset(x = offset, y = offset),
        end = Offset(x = offset, y = size.height - offset),
        strokeWidth = 5f,
    )
    drawLine(
        color = Color.Black,
        start = Offset(x = 0f, y = size.height - offset),
        end = Offset(x = size.width, y = size.height - offset),
        strokeWidth = 5f,
    )
    val hours = 0 .. 24 step 2
    val hoursCount = hours.last / hours.step
    val paint = Paint().apply {
        color = Color.Black
    }
    val graphWidth = size.width - offset
    hours.forEach { hour ->
        val index = hour / hours.step
        val hourWidth = graphWidth / hoursCount
        val x = offset + index * hourWidth
        val y = size.height - offset
        drawText(hour.toString(), x, y, fontSize, paint)
    }
}