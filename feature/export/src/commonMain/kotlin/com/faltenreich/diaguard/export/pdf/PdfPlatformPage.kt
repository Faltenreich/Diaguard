package com.faltenreich.diaguard.export.pdf

internal expect class PdfPlatformPage(
    document: PdfDocument,
    size: PdfSize,
) {

    fun drawText(text: String, position: PdfPosition, paint: PdfPaint)
}