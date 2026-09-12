package com.faltenreich.diaguard.export.pdf

import android.graphics.Paint
import android.graphics.Rect

internal class PdfText(
    private val text: String,
    private val paint: Paint,
) : PdfDrawable {

    override fun getSize(page: PdfPage): PdfSize {
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return PdfSize(bounds.width(), bounds.height())
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        page.draw { drawText(text, position, paint) }
    }
}