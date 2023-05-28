package com.faltenreich.diaguard.log.usecase

import com.faltenreich.diaguard.log.item.LogData
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateProgression

class GetLogDataUseCase {

    operator fun invoke(
        startDate: Date,
        endDate: Date,
    ): List<LogData> {
        println("Fetching log data between $startDate and $endDate")
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