package com.faltenreich.diaguard.export.form

import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory

class MapDateRangeUseCase(private val dateTimeFactory: DateTimeFactory) {

    operator fun invoke(dateRange: ExportDateRange): DateRange {
        val today = dateTimeFactory.today()
        return when (dateRange) {
            ExportDateRange.TODAY -> today .. today
            // FIXME: Start of calendar week
            ExportDateRange.WEEK_CURRENT -> today.minus(1, DateUnit.WEEK) .. today
            ExportDateRange.WEEKS_TWO -> today.minus(2, DateUnit.WEEK) .. today
            ExportDateRange.WEEKS_FOUR -> today.minus(4, DateUnit.WEEK) .. today
            // FIXME: Start of month
            ExportDateRange.MONTH_CURRENT -> today.minus(1, DateUnit.MONTH) .. today
            // FIXME: Start of quarter
            ExportDateRange.QUARTER_CURRENT -> today.minus(1, DateUnit.QUARTER) .. today
        }
    }
}