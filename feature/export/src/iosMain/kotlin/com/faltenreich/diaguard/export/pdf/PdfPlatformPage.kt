package com.faltenreich.diaguard.export.pdf

internal actual data class PdfPlatformPage actual constructor(
    private val document: PdfDocument,
    private val size: PdfSize,
) {

    actual fun finish() {
        TODO("Not yet implemented")
    }

    actual fun drawText(text: String, position: PdfPosition, paint: PdfPaint) {
        TODO("Not yet implemented")
    }
}