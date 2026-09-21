package com.faltenreich.diaguard.persistence.pdf

import androidx.compose.ui.graphics.Color

actual class PdfPaint actual constructor(
    color: Color,
    typeface: PdfTypeface,
    textSize: Float,
) {

    actual fun getTextBounds(text: String): PdfSize {
        TODO("Not yet implemented")
    }

    actual companion object {

        actual val normal: PdfPaint = TODO("Not yet implemented")
        actual val label: PdfPaint = TODO("Not yet implemented")
        actual val bold: PdfPaint = TODO("Not yet implemented")
        actual val header: PdfPaint = TODO("Not yet implemented")
        actual val background: PdfPaint = TODO("Not yet implemented")
        actual val divider: PdfPaint = TODO("Not yet implemented")
    }
}