package com.faltenreich.diaguard.export.pdf

import androidx.compose.ui.graphics.Color

internal expect class PdfPaint(
    color: Color,
    textSize: Float = 12f,
) {

    fun getTextBounds(text: String): PdfSize
}