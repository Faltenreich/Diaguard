package com.faltenreich.diaguard.export.pdf.print

import android.graphics.Canvas
import android.graphics.PointF
import android.util.Size
import com.faltenreich.diaguard.data.entry.Entry

internal class PdfTable(
    private val entries: List<Entry>
) : PdfDrawable {

    override fun drawOn(canvas: Canvas, position: PointF) {

    }

    override fun getSize(): Size {
        return Size(0, 0)
    }
}