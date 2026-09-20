package com.faltenreich.diaguard.export.pdf

import androidx.compose.ui.graphics.Color

internal expect class PdfPaint {

    val color: Color
    val typeface: PdfTypeface
    val textSize: Float

    companion object {

        val normal: PdfPaint
        val label: PdfPaint
        val bold: PdfPaint
        val header: PdfPaint
        val background: PdfPaint
    }
}