package com.faltenreich.diaguard.export.pdf.print

import android.graphics.PointF
import android.util.Size

internal class PdfLog : PdfDrawable {

    override fun getSize(page: PdfPage): Size {
        return Size(0, 0)
    }

    override fun drawOn(page: PdfPage, position: PointF) = Unit
}