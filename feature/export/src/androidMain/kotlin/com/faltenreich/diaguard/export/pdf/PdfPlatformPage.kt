package com.faltenreich.diaguard.export.pdf

internal actual data class PdfPlatformPage actual constructor(
    private val document: PdfDocument,
    private val size: PdfSize,
) {

    private val pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(
        size.width.toInt(),
        size.height.toInt(),
        document.pageCount,
    ).create()
    private val platform = document.platform.startPage(pageInfo)

    actual fun finish() {
        document.platform.finishPage(platform)
    }

    actual fun drawText(text: String, position: PdfPosition, paint: PdfPaint) {
        platform.canvas.drawText(text, position.x, position.y, paint.platform)
    }
}