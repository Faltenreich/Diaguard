package com.faltenreich.diaguard.shared.view

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path

fun Path.bezierBetween(start: Offset, end: Offset) {
    val control1 = Offset(
        x = (start.x + end.x) / 2,
        y = start.y,
    )
    val control2 = Offset(
        x = (start.x + end.x) / 2,
        y = end.y,
    )
    cubicTo(
        x1 = control1.x,
        y1 = control1.y,
        x2 = control2.x,
        y2 = control2.y,
        x3 = end.x,
        y3 = end.y,
    )
}