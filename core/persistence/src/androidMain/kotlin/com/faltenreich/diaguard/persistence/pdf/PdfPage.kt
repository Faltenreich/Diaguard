package com.faltenreich.diaguard.persistence.pdf

import android.graphics.pdf.PdfDocument.Page
import android.graphics.pdf.PdfDocument.PageInfo
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.graphics.withTranslation

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

    actual fun drawText(text: String, position: PdfPosition, maxWidth: Float?, paint: PdfPaint) {
        val canvas = actual.canvas
        val paint = paint.actual
        val x = position.x
        val y = position.y

        if (maxWidth != null) {
            canvas.withTranslation(x, y) {
                StaticLayout(
                    text,
                    TextPaint(paint),
                    maxWidth.toInt(),
                    Layout.Alignment.ALIGN_NORMAL,
                    1f,
                    0f,
                    false,
                ).draw(canvas)
            }
        } else {
            canvas.drawText(text, x, y - paint.fontMetrics.ascent, paint)
        }
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