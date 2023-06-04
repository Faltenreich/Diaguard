package com.faltenreich.diaguard.log

import app.cash.paging.PagingConfig
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
import com.faltenreich.diaguard.shared.view.isAppending
import com.faltenreich.diaguard.shared.view.isPrepending
import com.faltenreich.diaguard.shared.view.isRefreshing
import kotlinx.coroutines.flow.first

class LogItemSource(
    private val entryRepository: EntryRepository = inject(),
) : PagingSource<Date, LogItem>() {

    private lateinit var cache: Arguments

    private data class Arguments(
        val prevKey: Date,
        val nextKey: Date,
    )

    override fun getRefreshKey(state: PagingState<Date, LogItem>): Date? {
        println("LogViewModel: getRefreshKey for: $state")
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plusDays(1) ?: anchorPage.nextKey?.minusDays(1)
    }

    override suspend fun load(params: PagingSourceLoadParams<Date>): PagingSourceLoadResult<Date, LogItem> {
        val key = params.key ?: throw IllegalArgumentException("Missing key")
        val startDate: Date
        val endDate: Date

        when {
            params.isRefreshing() -> {
                startDate = key.minusDays(params.loadSize)
                endDate = key.plusDays(params.loadSize)
                cache = Arguments(prevKey = startDate, nextKey = endDate)
            }
            params.isPrepending() -> {
                startDate = key.minusDays(params.loadSize)
                endDate = key
                cache = cache.copy(prevKey = startDate)
            }
            params.isAppending() -> {
                startDate = key
                endDate = key.plusDays(params.loadSize)
                cache = cache.copy(nextKey = endDate)
            }
            else -> throw IllegalArgumentException("Unhandled parameters: $params")
        }

        println("LogViewModel: Fetching data for: $startDate - $endDate")
        val entries = entryRepository.getByDateRange(
            startDateTime = startDate.atTime(Time.atStartOfDay()),
            endDateTime = endDate.atTime(Time.atEndOfDay()),
        ).first()
        val items = DateProgression(startDate, endDate).map { date ->
            val headers = listOfNotNull(
                LogItem.MonthHeader(date).takeIf { date.dayOfMonth == 1 },
                LogItem.DayHeader(date),
            )
            val entriesOfDate = entries.filter { it.dateTime.date == date }
            val entryContent = entriesOfDate.takeIf(List<Entry>::isNotEmpty)?.map { LogItem.EntryContent(it) }
            val content = entryContent ?: listOf(LogItem.EmptyContent(date))
            headers + content
        }.flatten()
        val page = PagingSourceLoadResultPage(
            data = items,
            prevKey = cache.prevKey.minusDays(1),
            nextKey = cache.nextKey.plusDays(1),
            itemsBefore = 1, // TODO: Calculate placeholder size (page size?)
            itemsAfter = 1,
        )
        println("LogViewModel: Fetched data for $startDate - $endDate, previous: ${page.prevKey}, next: ${page.nextKey}")
        return page
    }

    companion object {

        private const val PAGE_SIZE_IN_DAYS = 20

        fun newConfig(): PagingConfig {
            return PagingConfig(pageSize = PAGE_SIZE_IN_DAYS, initialLoadSize = PAGE_SIZE_IN_DAYS)
        }
    }
}