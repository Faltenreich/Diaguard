package com.faltenreich.diaguard.export.pdf

import androidx.compose.ui.graphics.Color

internal data class PdfPaint(
    val color: Color,
    val typeface: PdfTypeface = PdfTypeface.NORMAL,
    val textSize: Float = 12f,
) {

    companion object {

        val normal: PdfPaint = PdfPaint(
            color = Color.Black,
            typeface = PdfTypeface.NORMAL,
        )
        val label: PdfPaint = PdfPaint(
            color = Color.DarkGray,
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
        val background: PdfPaint = PdfPaint(
            color = Color(0xFFF3F3F3),
        )
    }
}