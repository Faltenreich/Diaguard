package com.faltenreich.diaguard.export.pdf

data class PdfRectangle(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
) {

    val width: Float = right - left
    val height: Float = bottom - top
}