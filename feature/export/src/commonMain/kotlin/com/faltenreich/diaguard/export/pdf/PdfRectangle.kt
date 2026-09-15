package com.faltenreich.diaguard.export.pdf

data class PdfRectangle(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
) {

    val width: Float = right - left
    val height: Float = bottom - top

    constructor(position: PdfPosition, size: PdfSize) : this(
        left = position.x,
        top = position.y,
        right = position.x + size.width,
        bottom = position.y + size.height,
    )
}