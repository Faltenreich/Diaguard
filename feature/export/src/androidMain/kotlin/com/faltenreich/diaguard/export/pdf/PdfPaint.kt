package com.faltenreich.diaguard.export.pdf

import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

internal actual data class PdfPaint actual constructor(
    private val color: Color,
    private val textSize: Float,
) {

    val platformPaint: Paint = Paint().apply {
        color = this@PdfPaint.color.toArgb()
        // TODO: typeface = android.graphics.Typeface.create(typeface.systemFontFamilyName, typeface.style)
        textSize = this@PdfPaint.textSize
    }

    actual fun getTextBounds(text: String): PdfSize {
        val bounds = Rect()
        platformPaint.getTextBounds(text, 0, text.length, bounds)
        return PdfSize(bounds.width().toFloat(), bounds.height().toFloat())
    }
}