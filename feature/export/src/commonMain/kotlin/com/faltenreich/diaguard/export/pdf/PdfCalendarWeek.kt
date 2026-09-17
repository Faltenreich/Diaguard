package com.faltenreich.diaguard.export.pdf

import kotlin.math.max

internal data class PdfCalendarWeek(
    private val calendarWeek: PdfText,
    private val dateRange: PdfText,
) : PdfDrawable {

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