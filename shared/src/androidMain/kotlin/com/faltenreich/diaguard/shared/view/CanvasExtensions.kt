package com.faltenreich.diaguard.shared.view

import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas

actual fun DrawScope.drawText(
    text: String,
    x: Float,
    y: Float,
    paint: Paint,
) {
    drawContext.canvas.nativeCanvas.drawText(text, x, y, paint.asFrameworkPaint())
}