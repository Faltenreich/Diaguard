package com.faltenreich.diaguard.export.pdf.print

import android.graphics.Canvas
import android.graphics.PointF
import android.util.Size
import kotlin.math.max

internal data class PdfHeader(
    private val calendarWeek: PdfText,
    private val dateRange: PdfText,
) : PdfDrawable {

    override fun drawOn(canvas: Canvas, position: PointF) {
        calendarWeek.drawOn(canvas, position)
        dateRange.drawOn(canvas, PointF(position.x, position.y + calendarWeek.getSize().height))
    }

    override fun getSize(): Size {
        val calendarWeekSize = calendarWeek.getSize()
        val dateRangeSize = calendarWeek.getSize()
        return Size(
            max(calendarWeekSize.width, dateRangeSize.width),
            calendarWeekSize.height + dateRangeSize.height,
        )
    }
}