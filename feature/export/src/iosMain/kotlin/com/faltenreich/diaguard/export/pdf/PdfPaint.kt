package com.faltenreich.diaguard.export.pdf

import androidx.compose.ui.graphics.Color

internal actual data class PdfPaint actual constructor(
    private val color: Color,
    private val typeface: PdfTypeface,
) {

    actual fun getTextBounds(text: String): PdfSize {
        TODO("Not yet implemented")
    }
}