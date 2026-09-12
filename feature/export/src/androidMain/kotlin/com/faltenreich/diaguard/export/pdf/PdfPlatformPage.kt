package com.faltenreich.diaguard.export.pdf

import android.graphics.Rect
import android.graphics.pdf.PdfDocument.Page
import android.graphics.pdf.PdfDocument.PageInfo

internal actual class PdfPlatformPage actual constructor(
    private val document: PdfDocument,
    size: PdfSize,
) {

    private val page: Page = document.runNative {
        startPage(
            PageInfo.Builder(
                size.width.toInt(),
                size.height.toInt(),
                document.countPages(),
            ).create()
        )
    }

    actual fun finish() {
        document.runNative { finishPage(page) }
    }

    actual fun drawText(text: String, position: PdfPosition, paint: PdfPaint) {
        page.canvas.drawText(text, position.x, position.y, paint.toPlatform())
    }

    actual fun getTextBounds(text: String, paint: PdfPaint): PdfSize {
        val bounds = Rect()
        paint.toPlatform().getTextBounds(text, 0, text.length, bounds)
        return PdfSize(bounds.width().toFloat(), bounds.height().toFloat())
    }
}