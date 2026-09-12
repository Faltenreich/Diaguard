package com.faltenreich.diaguard.export.pdf

import androidx.compose.ui.graphics.Color

internal data class PdfPaint(
    val color: Color,
    val typeface: PdfTypeface,
    val textSize: Float = 12f,
) {

    companion object {

        val normal: PdfPaint = PdfPaint(
            color = Color.Black,
            typeface = PdfTypeface.NORMAL,
        )
        val bold: PdfPaint = PdfPaint(
            color = Color.Black,
            typeface = PdfTypeface.BOLD,
        )
        val header: PdfPaint = PdfPaint(
            color = Color.Black,
            typeface = PdfTypeface.HEADER,
            textSize = 14f,
        )
    }
}