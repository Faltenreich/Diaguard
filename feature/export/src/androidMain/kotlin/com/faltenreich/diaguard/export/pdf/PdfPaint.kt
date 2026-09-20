package com.faltenreich.diaguard.export.pdf

import androidx.compose.ui.graphics.Color

internal actual class PdfPaint(
    actual val color: Color,
    actual val typeface: PdfTypeface = PdfTypeface.NORMAL,
    actual val textSize: Float = 12f,
) {
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