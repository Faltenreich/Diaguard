package com.faltenreich.diaguard.export.pdf.print

import android.graphics.PointF
import android.util.Size

internal interface PdfDrawable {

    fun getSize(page: PdfPage): Size

    fun drawOn(page: PdfPage, position: PointF)
}