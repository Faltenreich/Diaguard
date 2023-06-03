package com.faltenreich.diaguard.log.usecase

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.log.LogPaginationState
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.shared.datetime.DateProgression
import com.faltenreich.diaguard.shared.datetime.Time
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class GetLogItemsUseCase(
    private val entryRepository: EntryRepository,
) {

    operator fun invoke(
        pagination: Flow<LogPaginationState>,
    ): Flow<List<LogItem>> {
        return pagination.map { pagination ->
            println("LogViewModel: Fetching data: $pagination")
            pagination to entryRepository.getByDateRange(
                startDateTime = pagination.minimumDate.atTime(Time.atStartOfDay()),
                endDateTime = pagination.maximumDate.atTime(Time.atEndOfDay()),
            ) // TODO: .deep()
        }.map { (pagination, entries) ->
            println("LogViewModel: Mapping data: $pagination")
            // TODO: Improve performance by somehow caching previous data
            val result = DateProgression(pagination.minimumDate, pagination.maximumDate).map { date ->
                val headers = listOfNotNull(
                    LogItem.MonthHeader(date).takeIf { date.dayOfMonth == 1 },
                    LogItem.DayHeader(date),
                )
                println("LogViewModel: Requesting entries for: $date")
                val entriesOfDate = entries.first().filter { it.dateTime.date == date }
                println("LogViewModel: Requested entries for: $date")
                val entryContent = entriesOfDate.takeIf(List<Entry>::isNotEmpty)?.map { LogItem.EntryContent(it) }
                val content = entryContent ?: listOf(LogItem.EmptyContent(date))
                headers + content
            }.flatten()
            println("LogViewModel: Got data: $pagination")
            result
        }
    }
}