package com.faltenreich.diaguard.export.pdf

internal expect class PdfPage(
    document: PdfDocument,
    size: PdfSize,
    viewport: PdfRectangle,
) {

    val viewport: PdfRectangle
    var offset: PdfPosition

    fun finish()

    fun canMove(by: Float): Boolean

    fun move(by: Float)

    fun drawText(text: String, position: PdfPosition, paint: PdfPaint)

    fun drawRectangle(rectangle: PdfRectangle, paint: PdfPaint)
}