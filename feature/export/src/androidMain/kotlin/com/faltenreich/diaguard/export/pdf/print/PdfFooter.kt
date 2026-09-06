package com.faltenreich.diaguard.export.pdf.print

import android.graphics.Canvas
import android.graphics.PointF
import android.util.Size

internal class PdfFooter(
    private val dateOfExport: PdfText?,
    private val pageNumber: PdfText?,
) : PdfDrawable {

    override fun drawOn(canvas: Canvas, position: PointF) {
        dateOfExport?.drawOn(canvas, position)
        pageNumber?.let {
            // TODO: Get parent width
            val x = 500f - pageNumber.getSize().width
            pageNumber.drawOn(canvas, PointF(x, position.y))
        }
    }

    override fun getSize(): Size {
        // TODO: Get size in parent
        return Size(0, 0)
    }
}