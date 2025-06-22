package com.faltenreich.diaguard.shared.view

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas

@Deprecated("Replace with androidx.compose.ui.text.TextPainter.drawText which fixes offset")
actual fun DrawScope.drawText(
    text: String,
    bottomLeft: Offset,
    size: Float,
    paint: Paint,
) {
    val nativePaint = paint.asFrameworkPaint().apply { textSize = size }
    drawContext.canvas.nativeCanvas.drawText(text, bottomLeft.x, bottomLeft.y, nativePaint)
}