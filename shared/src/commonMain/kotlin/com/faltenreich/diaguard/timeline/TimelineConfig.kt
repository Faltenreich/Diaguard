package com.faltenreich.diaguard.timeline

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

data class TimelineConfig(
    val padding: Float,
    val fontPaint: Paint,
    val fontSize: Float,

    val backgroundColor: Color,
    val cornerRadius: CornerRadius = CornerRadius(x = 20f, y = 20f),

    val gridStrokeColor: Color,
    val gridStrokeWidth: Float = 1f,
    val gridShadowColor: Color,

    val valueColorNormal: Color,
    val valueColorLow: Color,
    val valueColorHigh: Color,
    val valueStrokeWidth: Float = 8f,
    val valueDotRadius: Float = 16f,
) {

    val valueStroke: Stroke = Stroke(width = valueStrokeWidth)
    val valuePath: Path = Path()
}