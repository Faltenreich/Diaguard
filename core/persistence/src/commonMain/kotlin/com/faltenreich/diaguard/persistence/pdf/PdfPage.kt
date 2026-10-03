package com.faltenreich.diaguard.persistence.pdf

import androidx.compose.ui.graphics.Color

expect class PdfPage(
    document: PdfDocument,
    size: PdfSize,
    viewport: PdfRectangle,
) {

    val viewport: PdfRectangle
    var offset: PdfPosition

    fun finish()

    fun canMove(by: Float): Boolean

    fun move(by: Float)

    fun drawText(text: String, position: PdfPosition, maxWidth: Float?, paint: PdfPaint)

    fun drawRectangle(rectangle: PdfRectangle, paint: PdfPaint)

    fun drawLine(start: PdfPosition, end: PdfPosition, color: Color, width: Float)
}