package com.faltenreich.diaguard.log.usecase

import app.cash.paging.PagingSource
import app.cash.paging.PagingSourceLoadParams
import app.cash.paging.PagingSourceLoadResult
import app.cash.paging.PagingSourceLoadResultPage
import app.cash.paging.PagingState
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateProgression
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.first

class LogItemPagingSource(
    private val initialDate: Date,
    private val entryRepository: EntryRepository = inject(),
) : PagingSource<Date, LogItem>() {

    override fun getRefreshKey(state: PagingState<Date, LogItem>): Date? {
        val position = state.anchorPosition ?: return null
        return state.closestItemToPosition(position)?.date
    }

    override suspend fun load(params: PagingSourceLoadParams<Date>): PagingSourceLoadResult<Date, LogItem> {
        val startDate = params.key ?: initialDate
        val endDate = startDate.plusDays(params.loadSize)
        val entries = entryRepository.getByDateRange(
            startDateTime = startDate.atTime(Time.atStartOfDay()),
            endDateTime = endDate.atTime(Time.atEndOfDay()),
        ).first()
        val items = DateProgression(startDate, endDate).map { date ->
            val headers = listOfNotNull(
                LogItem.MonthHeader(date).takeIf { date.dayOfMonth == 1 },
                LogItem.DayHeader(date),
            )
            println("LogViewModel: Requesting entries for: $date")
            val entriesOfDate = entries.filter { it.dateTime.date == date }
            println("LogViewModel: Requested entries for: $date")
            val entryContent = entriesOfDate.takeIf(List<Entry>::isNotEmpty)?.map { LogItem.EntryContent(it) }
            val content = entryContent ?: listOf(LogItem.EmptyContent(date))
            headers + content
        }.flatten()
        return PagingSourceLoadResultPage(
            data = items,
            prevKey = if (startDate == initialDate) null else initialDate.minusDays(1),
            nextKey = endDate.plusDays(1),
        )
    }
}