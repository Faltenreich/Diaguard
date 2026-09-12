package com.faltenreich.diaguard.export.pdf

import androidx.compose.ui.graphics.Color

internal expect class PdfPaint(
    color: Color,
    typeface: PdfTypeface,
) {

    fun getTextBounds(text: String): PdfSize
}