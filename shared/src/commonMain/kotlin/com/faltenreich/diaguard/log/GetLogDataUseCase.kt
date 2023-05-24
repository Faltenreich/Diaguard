package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.shared.datetime.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class GetLogDataUseCase(
    private val entryRepository: EntryRepository,
) {

    operator fun invoke(
        startDate: Date,
        endDate: Date,
    ): Flow<List<LogData>> {
        val dateRange = startDate.dayOfMonth .. endDate.dayOfMonth
        return dateRange.asFlow()
            .map { dayOfMonth ->
                val date = startDate // TODO: Calculate dates
                val entriesOfDay = entryRepository.getByDate(date).first()
                val entryContent = entriesOfDay.map { LogData.EntryContent(it) }
                listOfNotNull(
                    LogData.MonthHeader(date).takeIf { dayOfMonth == 0 },
                    LogData.DayHeader(date),
                ) + entryContent
            }
    }
}