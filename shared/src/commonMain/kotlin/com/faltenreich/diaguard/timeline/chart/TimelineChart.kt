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
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.unit.dp
import com.faltenreich.diaguard.shared.view.drawText

private const val Y_MIN = 0
private const val Y_MAX = 250
private const val X_MIN = 0
private const val X_MAX = 24

private val AXIS_OFFSET = 40.dp

@OptIn(ExperimentalTextApi::class)
@Composable
fun TimelineChart(
    modifier: Modifier = Modifier,
) {
    val offset = LocalDensity.current.run { AXIS_OFFSET.toPx() }
    Canvas(
        modifier = modifier.fillMaxSize(),
    ) {
        drawAxis(offset = offset)
    }
}

private fun DrawScope.drawAxis(offset: Float) {
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
    val paint = Paint().apply { color = Color.Black }
    hours.forEach { hour ->
        drawText("Hello, World", x = 200f, y = 200f, paint)
    }
}