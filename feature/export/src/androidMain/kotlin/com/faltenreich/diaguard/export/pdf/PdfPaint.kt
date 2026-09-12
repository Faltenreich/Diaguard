package com.faltenreich.diaguard.export.pdf

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

internal actual data class PdfPaint actual constructor(
    private val color: Color,
    private val typeface: PdfTypeface,
) {

    val platform: Paint = Paint().apply {
        color = this@PdfPaint.color.toArgb()
        typeface = when (this@PdfPaint.typeface) {
            PdfTypeface.NORMAL -> Typeface.DEFAULT
            PdfTypeface.BOLD,
            PdfTypeface.HEADER -> Typeface.DEFAULT_BOLD
        }
        if (this@PdfPaint.typeface == PdfTypeface.HEADER) {
            textSize = 14f
        }
    }

    actual fun getTextBounds(text: String): PdfSize {
        val bounds = Rect()
        platform.getTextBounds(text, 0, text.length, bounds)
        return PdfSize(bounds.width().toFloat(), bounds.height().toFloat())
    }
}