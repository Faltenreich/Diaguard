package com.faltenreich.diaguard.log.usecase

import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateProgression
import com.faltenreich.diaguard.shared.datetime.Time
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class GetLogItemsUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val entryRepository: EntryRepository,
) {

    suspend operator fun invoke(
        startDate: Date,
        endDate: Date,
    ): List<LogItem> = withContext(dispatcher) {
        println("Fetching log data between $startDate and $endDate")
        val entries = entryRepository.getByDateRange(
            startDateTime = startDate.atTime(Time.atStartOfDay()),
            endDateTime = endDate.atTime(Time.atEndOfDay()),
        ).first().groupBy { it.dateTime.date }
        DateProgression(startDate, endDate).map { date ->
            val headers = listOfNotNull(
                LogItem.MonthHeader(date).takeIf { date.dayOfMonth == 1 },
                LogItem.DayHeader(date),
            )
            val entriesOfDate = entries[date]
            val entryContent = entriesOfDate
                ?.takeIf(List<*>::isNotEmpty)
                ?.map { LogItem.EntryContent(it) }
            val content = entryContent ?: listOf(LogItem.EmptyContent(date))
            headers + content
        }.flatten()
    }
}