package com.faltenreich.diaguard.persistence.pdf

import androidx.compose.ui.graphics.Color

expect class PdfPaint(
    color: Color = Color.Black,
    typeface: PdfTypeface = PdfTypeface.NORMAL,
    textSize: Float = 12f,
) {

    fun getTextBounds(text: String, maxWidth: Float?): PdfSize

    companion object {

        val normal: PdfPaint
        val label: PdfPaint
        val bold: PdfPaint
        val header: PdfPaint
        val background: PdfPaint
        val divider: PdfPaint
    }
}