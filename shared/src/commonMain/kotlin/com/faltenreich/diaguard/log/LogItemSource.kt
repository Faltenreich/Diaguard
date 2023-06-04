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

    private data class Cache(
        val startDate: Date,
        val endDate: Date,
    )

    private lateinit var cache: Cache

    override fun getRefreshKey(state: PagingState<Date, LogItem>): Date? {
        println("LogViewModel: getRefreshKey for: $state")
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        // FIXME: Determine refresh key to fix pagination invalidation on invalidation, e.g. when deleting item
        return anchorPage.prevKey
    }

    override suspend fun load(params: PagingSourceLoadParams<Date>): PagingSourceLoadResult<Date, LogItem> {
        val key = params.key ?: throw IllegalArgumentException("Missing key")
        val startDate: Date
        val endDate: Date
        when {
            params.isRefreshing() -> {
                // FIXME: Leads to delayed sticky header as start of month will be loaded afterwards
                startDate = key
                endDate = key.plusDays(params.loadSize)
                cache = Cache(startDate = startDate, endDate = endDate)
            }
            params.isPrepending() -> {
                startDate = key.minusDays(params.loadSize)
                endDate = key
                cache = cache.copy(startDate = startDate)
            }
            params.isAppending() -> {
                startDate = key
                endDate = key.plusDays(params.loadSize)
                cache = cache.copy(endDate = endDate)
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
            prevKey = cache.startDate.minusDays(1),
            nextKey = cache.endDate.plusDays(1),
            // FIXME: Leads to endless pagination due to stuck scroll position
            // itemsBefore = 1,
            itemsAfter = PAGE_SIZE_IN_DAYS,
        )
        println("LogViewModel: Fetched data for $startDate - $endDate, previous: ${page.prevKey}, next: ${page.nextKey}")
        return page
    }

    companion object {

        // FIXME: Leads to delayed sticky headers as start of month will potentially not be included on pagination
        private const val PAGE_SIZE_IN_DAYS = 20

        fun newConfig(): PagingConfig {
            return PagingConfig(
                pageSize = PAGE_SIZE_IN_DAYS,
                // prefetchDistance = 1,
            )
        }
    }
}