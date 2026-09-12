package com.faltenreich.diaguard.export.pdf.print

import android.graphics.PointF
import android.util.Size
import kotlin.math.max

internal data class PdfHeader(
    private val calendarWeek: PdfText,
    private val dateRange: PdfText,
) : PdfDrawable {

    override fun drawOn(page: PdfPage, position: PointF) {
        calendarWeek.drawOn(page, position)
        dateRange.drawOn(page, PointF(position.x, position.y + calendarWeek.getSize(page).height))
    }

    override fun getSize(page: PdfPage): Size {
        val calendarWeekSize = calendarWeek.getSize(page)
        val dateRangeSize = calendarWeek.getSize(page)
        return Size(
            max(calendarWeekSize.width, dateRangeSize.width),
            calendarWeekSize.height + dateRangeSize.height,
        )
    }
}