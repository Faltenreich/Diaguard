package com.faltenreich.diaguard.export.pdf

import kotlin.math.max

internal class PdfHeader(
    calendarWeek: String,
    dateRange: String,
) : PdfDrawable {

    private val calendarWeek = PdfText(calendarWeek, PdfPaint.header)
    private val dateRange = PdfText(dateRange, PdfPaint.normal)

    private val spacing = PdfSpacing.P_8.points

    override fun getSize(page: PdfPage): PdfSize {
        val calendarWeekSize = calendarWeek.getSize(page)
        val dateRangeSize = calendarWeek.getSize(page)
        return PdfSize(
            width = max(calendarWeekSize.width, dateRangeSize.width),
            height = calendarWeekSize.height + spacing + dateRangeSize.height + PdfSpacing.HEADER_PADDING_BOTTOM.points,
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        calendarWeek.drawOn(page, position)
        dateRange.drawOn(
            page,
            PdfPosition(
                position.x,
                position.y + spacing + calendarWeek.getSize(page).height,
            )
        )
    }
}