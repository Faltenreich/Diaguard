package com.faltenreich.diaguard.persistence.pdf

actual class PdfPage actual constructor(
    document: PdfDocument,
    size: PdfSize,
    actual val viewport: PdfRectangle,
) {

    actual var offset: PdfPosition = PdfPosition(x = viewport.left, y = viewport.top)

    actual fun finish() {
        TODO("Not yet implemented")
    }

    actual fun canMove(by: Float): Boolean {
        return offset.y + by <= viewport.bottom
    }

    actual fun move(by: Float) {
        offset = offset.copy(x = offset.x, y = offset.y + by)
    }

    actual fun drawText(text: String, position: PdfPosition, paint: PdfPaint) {
        TODO("Not yet implemented")
    }

    actual fun drawRectangle(rectangle: PdfRectangle, paint: PdfPaint) {
        TODO("Not yet implemented")
    }
}