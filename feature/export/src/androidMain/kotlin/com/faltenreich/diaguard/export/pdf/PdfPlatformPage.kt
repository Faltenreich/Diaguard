package com.faltenreich.diaguard.export.pdf

internal actual data class PdfPlatformPage actual constructor(
    private val document: PdfDocument,
    private val size: PdfSize,
) {

    private val pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(
        size.width.toInt(),
        size.height.toInt(),
        0, // TODO
    ).create()
    private val page: android.graphics.pdf.PdfDocument.Page =
        document.platformDocument.document.startPage(pageInfo)

    actual fun finish() {
        document.platformDocument.document.finishPage(page)
    }

    actual fun drawText(text: String, position: PdfPosition, paint: PdfPaint) {
        page.canvas.drawText(text, position.x, position.y, paint.platformPaint)
    }
}