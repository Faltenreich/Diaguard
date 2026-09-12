package com.faltenreich.diaguard.export.pdf

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Typeface

internal expect class PdfPaint(
    color: Color,
    typeface: Typeface,
    textSize: Float = 12f,
) {

    fun getTextBounds(text: String): PdfSize

    fun drawText(text: String, position: PdfPosition)
}