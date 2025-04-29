package com.faltenreich.diaguard.export.form

import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory

class MapDateRangeUseCase(private val dateTimeFactory: DateTimeFactory) {

    operator fun invoke(dateRange: ExportDateRange): DateRange {
        val today = dateTimeFactory.today()
        return when (dateRange) {
            ExportDateRange.TODAY -> today .. today
            ExportDateRange.WEEK_CURRENT -> TODO()
            ExportDateRange.WEEKS_TWO -> TODO()
            ExportDateRange.WEEKS_FOUR -> TODO()
            ExportDateRange.MONTH_CURRENT -> TODO()
            ExportDateRange.QUARTER_CURRENT -> TODO()
        }
    }
}