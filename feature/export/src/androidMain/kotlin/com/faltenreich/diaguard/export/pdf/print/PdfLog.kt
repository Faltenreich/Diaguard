package com.faltenreich.diaguard.export.pdf.print

import android.graphics.Canvas
import android.graphics.PointF
import android.util.Size

internal class PdfLog : PdfDrawable {

    override fun drawOn(canvas: Canvas, position: PointF) = Unit

    override fun getSize(): Size {
        return Size(0, 0)
    }
}