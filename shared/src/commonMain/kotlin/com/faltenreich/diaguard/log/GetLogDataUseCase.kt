package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.shared.datetime.Date

class GetLogDataUseCase {

    operator fun invoke(
        startDate: Date,
        endDate: Date,
    ): List<LogData> {
        val dateRange = startDate.dayOfMonth .. endDate.dayOfMonth
        return dateRange.map { dayOfMonth ->
            val date = startDate // TODO: Calculate dates
            listOfNotNull(
                LogData.MonthHeader(date).takeIf { dayOfMonth == 0 },
                LogData.DayHeader(date),
                LogData.EmptyContent(date)
            )
        }.flatten()
    }
}