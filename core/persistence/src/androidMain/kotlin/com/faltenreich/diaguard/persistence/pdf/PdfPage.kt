package com.faltenreich.diaguard.persistence.pdf

import android.graphics.pdf.PdfDocument.Page
import android.graphics.pdf.PdfDocument.PageInfo
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint

actual class PdfPage actual constructor(
    private val document: PdfDocument,
    size: PdfSize,
    actual val viewport: PdfRectangle,
) {

    actual var offset: PdfPosition = PdfPosition(x = viewport.left, y = viewport.top)

    private val actual: Page = document.runNative {
        startPage(
            PageInfo.Builder(
                size.width.toInt(),
                size.height.toInt(),
                document.countPages(),
            ).create()
        )
    }

    actual fun finish() {
        document.runNative { finishPage(actual) }
    }

    actual fun canMove(by: Float): Boolean {
        return offset.y + by <= viewport.bottom
    }

    actual fun move(by: Float) {
        offset = offset.copy(x = offset.x, y = offset.y + by)
    }

    actual fun drawText(text: String, position: PdfPosition, size: PdfSize, paint: PdfPaint) {
        val paint = paint.actual
        val x = position.x
        val y = position.y

        @Suppress("Deprecation")
        val layout = StaticLayout(
            text,
            TextPaint(paint),
            size.width.toInt(),
            Layout.Alignment.ALIGN_NORMAL,
            1f,
            0f,
            false,
        )
        actual.canvas.save()
        actual.canvas.translate(x, y)
        layout.draw(actual.canvas)
        actual.canvas.restore()
    }

    actual fun drawRectangle(rectangle: PdfRectangle, paint: PdfPaint) {
        actual.canvas.drawRect(
            rectangle.left,
            rectangle.top,
            rectangle.right,
            rectangle.bottom,
            paint.actual,
        )
    }
}