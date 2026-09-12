package com.faltenreich.diaguard.export.pdf

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.ui.graphics.toArgb

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
        platform.canvas.drawText(text, position.x, position.y, paint.toPlatform())
    }

    actual fun getTextBounds(text: String, paint: PdfPaint): PdfSize {
        val bounds = Rect()
        paint.toPlatform().getTextBounds(text, 0, text.length, bounds)
        return PdfSize(bounds.width().toFloat(), bounds.height().toFloat())
    }

    private fun PdfPaint.toPlatform(): Paint {
        val paint = this
        return Paint().apply {
            color = paint.color.toArgb()
            typeface = when (paint.typeface) {
                PdfTypeface.NORMAL -> Typeface.DEFAULT
                PdfTypeface.BOLD,
                PdfTypeface.HEADER -> Typeface.DEFAULT_BOLD
            }
            if (paint.typeface == PdfTypeface.HEADER) {
                textSize = 14f
            }
        }
    }
}