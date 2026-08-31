package com.faltenreich.diaguard.export.pdf.print

import android.graphics.Canvas

internal interface PdfPrintable {

    fun drawOn(canvas: Canvas)
}