package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateProgression

class GetLogDataUseCase {

    operator fun invoke(
        startDate: Date,
        endDate: Date,
    ): List<LogData> {
        val dateRange = DateProgression(startDate, endDate)
        return dateRange.map { date ->
            listOfNotNull(
                LogData.MonthHeader(date).takeIf { date.dayOfMonth == 1 },
                LogData.DayHeader(date),
                LogData.EmptyContent(date)
            )
        }.flatten()
    }
}