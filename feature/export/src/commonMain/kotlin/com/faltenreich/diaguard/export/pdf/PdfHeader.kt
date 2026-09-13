package com.faltenreich.diaguard.export.pdf

import kotlin.math.max

internal data class PdfHeader(
    private val calendarWeek: PdfText,
    private val dateRange: PdfText,
) : PdfDrawable {

    private val spacing = PdfSpacing.P_8

    override fun getSize(page: PdfPage): PdfSize {
        val calendarWeekSize = calendarWeek.getSize(page)
        val dateRangeSize = calendarWeek.getSize(page)
        return PdfSize(
            max(calendarWeekSize.width, dateRangeSize.width),
            calendarWeekSize.height + spacing.points + dateRangeSize.height,
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        calendarWeek.drawOn(page, position)
        dateRange.drawOn(
            page,
            PdfPosition(
                position.x,
                position.y + spacing.points + calendarWeek.getSize(page).height,
            )
        )
    }
}