package com.faltenreich.diaguard.export.pdf.print

import android.graphics.Canvas
import android.graphics.PointF
import android.util.Size

internal interface PdfDrawable {

    fun getSize(): Size

    fun drawOn(canvas: Canvas, position: PointF)
}