package com.faltenreich.diaguard.export.pdf.print

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.util.Size

internal class PdfText(
    private val text: String,
    private val paint: Paint,
) : PdfDrawable {

    override fun getSize(): Size {
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return Size(bounds.width(), bounds.height())
    }

    override fun drawOn(canvas: Canvas, position: PointF) {
        canvas.drawText(text, position, paint)
    }
}