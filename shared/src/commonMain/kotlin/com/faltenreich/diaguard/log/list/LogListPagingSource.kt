package com.faltenreich.diaguard.log.list

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateProgression
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.list.MapEntryListItemStateUseCase
import com.faltenreich.diaguard.log.list.item.LogDayStyle
import com.faltenreich.diaguard.log.list.item.LogItemState
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.isPrepending

class LogListPagingSource(
    getTodayUseCase: GetTodayUseCase = inject(),
    private val entryRepository: EntryRepository = inject(),
    private val mapEntryListItemState: MapEntryListItemStateUseCase = inject(),
    private val formatter: DateTimeFormatter = inject(),
) : PagingSource<Date, LogItemState>() {

    private val today = getTodayUseCase()

    override fun getRefreshKey(state: PagingState<Date, LogItemState>): Date? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1, DateUnit.DAY)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1, DateUnit.DAY)
        }
    }

    override suspend fun load(params: LoadParams<Date>): LoadResult<Date, LogItemState> {
        // FIXME: Jumps down on subsequent refreshing, even though key seems correct
        val key = params.key ?: today
        val startDate: Date
        val endDate: Date
        when {
            params.isPrepending() -> {
                startDate = key.minus(params.loadSize, DateUnit.DAY)
                endDate = key
            }
            else -> {
                startDate = key
                endDate = key.plus(params.loadSize, DateUnit.DAY)
            }
        }

        val entries = entryRepository.getByDateRange(
            startDateTime = startDate.atStartOfDay(),
            endDateTime = endDate.atEndOfDay(),
        )

        val items = DateProgression(startDate, endDate).map { date ->
            val dateLocalized = formatter.formatMonthOfYear(date.monthOfYear, abbreviated = false)
            val headers = listOfNotNull(LogItemState.MonthHeader(date, dateLocalized).takeIf { date.dayOfMonth == 1 })
            val entriesOfDate = entries.filter { it.dateTime.date == date }
            val entryContent = entriesOfDate.takeIf(List<Entry>::isNotEmpty)?.map { entry ->
                LogItemState.EntryContent(
                    entryState = mapEntryListItemState(entry, includeDate = false),
                    style = LogDayStyle(
                        isVisible = entry == entriesOfDate.first(),
                        isHighlighted = entry.dateTime.date == today,
                    ),
                )
            }
            val content = entryContent ?: listOf(
                LogItemState.EmptyContent(
                    date = date,
                    style = LogDayStyle(
                        isVisible = true,
                        isHighlighted = date == today,
                    ),
                ),
            )
            headers + content
        }.flatten()

        val page = LoadResult.Page(
            data = items,
            prevKey = startDate.minus(1, DateUnit.DAY),
            nextKey = endDate.plus(1, DateUnit.DAY),
        )
        return page
    }

    companion object {

        private const val PAGE_SIZE_IN_DAYS = 10

        fun newConfig(): PagingConfig {
            return PagingConfig(pageSize = PAGE_SIZE_IN_DAYS)
        }
    }
}