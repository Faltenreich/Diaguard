package com.faltenreich.diaguard.export.pdf

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
        val paint = paint.paint
        val x = position.x
        val y = position.y - paint.fontMetrics.ascent
        page.canvas.drawText(text, x, y, paint)
    }

    actual fun drawRectangle(rectangle: PdfRectangle, paint: PdfPaint) {
        page.canvas.drawRect(
            rectangle.left,
            rectangle.top,
            rectangle.right,
            rectangle.bottom,
            paint.paint,
        )
    }
}