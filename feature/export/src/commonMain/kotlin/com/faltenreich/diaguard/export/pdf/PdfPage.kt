package com.faltenreich.diaguard.export.pdf

internal class PdfPage(
    document: PdfDocument,
    size: PdfSize,
    val viewport: PdfRectangle,
) {

    var offset: PdfPosition = PdfPosition(x = viewport.left, y = viewport.top)
    private val platformPage: PdfPlatformPage = PdfPlatformPage(document, size)

    fun finish() {
        platformPage.finish()
    }

    fun canMove(by: Float): Boolean {
        return offset.y + by <= viewport.bottom
    }

    fun move(by: Float) {
        offset = offset.copy(x = offset.x, y = offset.y + by)
    }

    fun drawText(text: String, position: PdfPosition, paint: PdfPaint) {
        platformPage.drawText(text, position, paint)
    }

    fun drawRectangle(rectangle: PdfRectangle, paint: PdfPaint) {
        platformPage.drawRectangle(rectangle, paint)
    }
}