package com.faltenreich.diaguard.export.pdf

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument.Page
import android.graphics.pdf.PdfDocument.PageInfo
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

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
        val paint = paint.toPlatform()
        val x = position.x
        val y = position.y - paint.fontMetrics.ascent
        page.canvas.drawText(text, x, y, paint)
    }

    actual fun getTextBounds(text: String, paint: PdfPaint): PdfSize {
        val bounds = Rect()
        paint.toPlatform().getTextBounds(text, 0, text.length, bounds)
        return PdfSize(bounds.width().toFloat(), bounds.height().toFloat())
    }

    actual fun drawRectangle(rectangle: PdfRectangle, paint: PdfPaint) {
        page.canvas.drawRect(
            rectangle.left,
            rectangle.top,
            rectangle.right,
            rectangle.bottom,
            paint.toPlatform(),
        )
    }

    private fun Color.toPlatform(): Int {
        return toArgb()
    }

    private fun PdfPaint.toPlatform(): Paint {
        val paint = this
        return Paint().apply {
            color = paint.color.toPlatform()
            typeface = when (paint.typeface) {
                PdfTypeface.NORMAL -> Typeface.DEFAULT
                PdfTypeface.BOLD,
                PdfTypeface.HEADER -> Typeface.DEFAULT_BOLD
            }
            textSize = paint.textSize
        }
    }
}