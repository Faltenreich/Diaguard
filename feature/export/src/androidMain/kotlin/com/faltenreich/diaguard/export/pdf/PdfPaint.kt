package com.faltenreich.diaguard.export.pdf

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

internal actual class PdfPaint(
    val color: Color,
    val typeface: PdfTypeface = PdfTypeface.NORMAL,
    val textSize: Float = 12f,
) {

    val actual: Paint = Paint().apply {
        val paint = this@PdfPaint
        color = paint.color.toArgb()
        typeface = when (paint.typeface) {
            PdfTypeface.NORMAL -> Typeface.DEFAULT
            PdfTypeface.BOLD,
            PdfTypeface.HEADER -> Typeface.DEFAULT_BOLD
        }
        textSize = paint.textSize
    }

    actual fun getTextBounds(text: String): PdfSize {
        val bounds = Rect()
        actual.getTextBounds(text, 0, text.length, bounds)
        return PdfSize(bounds.width().toFloat(), bounds.height().toFloat())
    }

    actual companion object {

        actual val normal: PdfPaint = PdfPaint(
            color = Color.Black,
            typeface = PdfTypeface.NORMAL,
        )
        actual val label: PdfPaint = PdfPaint(
            color = Color.DarkGray,
            typeface = PdfTypeface.NORMAL,
        )
        actual val bold: PdfPaint = PdfPaint(
            color = Color.Black,
            typeface = PdfTypeface.BOLD,
        )
        actual val header: PdfPaint = PdfPaint(
            color = Color.Black,
            typeface = PdfTypeface.HEADER,
            textSize = 14f,
        )
        actual val background: PdfPaint = PdfPaint(
            color = Color(0xFFF3F3F3),
        )
    }
}