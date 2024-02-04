package com.faltenreich.diaguard.log

import app.cash.paging.PagingConfig
import app.cash.paging.PagingSource
import app.cash.paging.PagingSourceLoadParams
import app.cash.paging.PagingSourceLoadResult
import app.cash.paging.PagingSourceLoadResultPage
import app.cash.paging.PagingState
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.deep
import com.faltenreich.diaguard.log.item.LogDayStyle
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateProgression
import com.faltenreich.diaguard.shared.datetime.DateUnit
import com.faltenreich.diaguard.shared.datetime.GetTodayUseCase
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.logging.Logger
import com.faltenreich.diaguard.shared.view.isAppending
import com.faltenreich.diaguard.shared.view.isPrepending
import com.faltenreich.diaguard.shared.view.isRefreshing

class LogItemSource(
    getTodayUseCase: GetTodayUseCase = inject(),
    private val entryRepository: EntryRepository = inject(),
) : PagingSource<Date, LogItem>() {

    private data class Cache(
        val startDate: Date,
        val endDate: Date,
    )

    private lateinit var cache: Cache
    private val today = getTodayUseCase()

    override fun getRefreshKey(state: PagingState<Date, LogItem>): Date? {
        Logger.debug("LogViewModel: getRefreshKey for: $state")
        // FIXME: Calculate correct refresh key to avoid jumps after having scrolled down
        val anchorPosition = state.anchorPosition ?: return null
        return state.closestItemToPosition(1)?.date
    }

    override suspend fun load(params: PagingSourceLoadParams<Date>): PagingSourceLoadResult<Date, LogItem> {
        val key = params.key ?: throw IllegalArgumentException("Missing key")
        val startDate: Date
        val endDate: Date
        when {
            params.isRefreshing() -> {
                // FIXME: Leads to delayed sticky header as start of month will be loaded afterwards
                startDate = key
                endDate = key.plus(params.loadSize, DateUnit.DAY)
                cache = Cache(startDate = startDate, endDate = endDate)
            }
            params.isPrepending() -> {
                startDate = key.minus(params.loadSize, DateUnit.DAY)
                endDate = key
                cache = cache.copy(startDate = startDate)
            }
            params.isAppending() -> {
                startDate = key
                endDate = key.plus(params.loadSize, DateUnit.DAY)
                cache = cache.copy(endDate = endDate)
            }
            else -> throw IllegalArgumentException("Unhandled parameters: $params")
        }
        Logger.debug("LogViewModel: Fetching data for: $startDate - $endDate")

        val entries = entryRepository.getByDateRange(
            startDateTime = startDate.atStartOfDay(),
            endDateTime = endDate.atEndOfDay(),
        ).deep()

        val items = DateProgression(startDate, endDate).map { date ->
            val headers = listOfNotNull(LogItem.MonthHeader(date).takeIf { date.dayOfMonth == 1 })
            val entriesOfDate = entries.filter { it.dateTime.date == date }
            val entryContent = entriesOfDate.takeIf(List<Entry>::isNotEmpty)?.map { entry ->
                LogItem.EntryContent(
                    entry = entry,
                    style = when {
                        entry != entriesOfDate.first() -> LogDayStyle.HIDDEN
                        entry.dateTime.date == today -> LogDayStyle.HIGHLIGHTED
                        else -> LogDayStyle.NORMAL
                    },
                )
            }
            val content = entryContent ?: listOf(
                LogItem.EmptyContent(
                    date = date,
                    style = if (date == today) LogDayStyle.HIGHLIGHTED else LogDayStyle.NORMAL,
                ),
            )
            headers + content
        }.flatten()

        val page = PagingSourceLoadResultPage(
            data = items,
            prevKey = cache.startDate.minus(1, DateUnit.DAY),
            nextKey = cache.endDate.plus(1, DateUnit.DAY),
            // FIXME: Prepending placeholders lead to endless pagination due to stuck scroll position
            // itemsBefore = 1,
            itemsAfter = PAGE_SIZE_IN_DAYS,
        )
        Logger.debug("LogViewModel: Fetched data for $startDate - $endDate, " +
            "previous: ${page.prevKey}, next: ${page.nextKey}")
        return page
    }

    companion object {

        // Must be at least the maximum day count per month to ensure a prepending header
        private const val PAGE_SIZE_IN_DAYS = 31

        fun newConfig(): PagingConfig {
            return PagingConfig(
                pageSize = PAGE_SIZE_IN_DAYS,
                // prefetchDistance = 1,
            )
        }
    }
}