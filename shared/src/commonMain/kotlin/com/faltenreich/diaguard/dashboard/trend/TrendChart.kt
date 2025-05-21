package com.faltenreich.diaguard.dashboard.trend

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.dashboard.DashboardState
import com.faltenreich.diaguard.shared.view.drawText

@Composable
fun TrendChart(
    state: DashboardState.Trend,
    modifier: Modifier = Modifier,
) = with(state) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val typography = AppTheme.typography
    val fontPaint = Paint().apply { color = colorScheme.onSurfaceVariant }
    val fontSize = density.run { typography.bodyMedium.fontSize.toPx() }

    Canvas(
        modifier = modifier.fillMaxSize(),
    ) {
        val widthPerDay = size.width / days.size
        days.forEachIndexed { index, day ->
            val text = day.dateLocalized
            val textSize = textMeasurer.measure(text).size
            val x = (widthPerDay * index) + (widthPerDay / 2) - (textSize.width / 2)
            val y = size.height - (textSize.height / 2)
            drawText(
                text = text,
                x = x,
                y = y,
                size = fontSize,
                paint = fontPaint,
            )
        }
    }
}