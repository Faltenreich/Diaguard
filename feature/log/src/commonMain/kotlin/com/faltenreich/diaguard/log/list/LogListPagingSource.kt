package com.faltenreich.diaguard.log.list

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.entry.EntryRepository
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateProgression
import com.faltenreich.diaguard.datetime.DateTimeConstants
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.entry.list.MapEntryListItemStateUseCase
import com.faltenreich.diaguard.injection.inject
import com.faltenreich.diaguard.log.list.item.LogDayState
import com.faltenreich.diaguard.log.list.item.LogDayStyle
import com.faltenreich.diaguard.log.list.item.LogItemState

class LogListPagingSource(
    getTodayUseCase: GetTodayUseCase = inject(),
    private val entryRepository: EntryRepository = inject(),
    private val mapEntryListItemState: MapEntryListItemStateUseCase = inject(),
    private val formatter: DateTimeFormatter = inject(),
) : PagingSource<Date, LogItemState>() {

    private val today = getTodayUseCase()

    // FIXME: Restore scroll offset instead of jumping to page start on refresh
    override fun getRefreshKey(state: PagingState<Date, LogItemState>): Date? {
        // Return startDate of currently visible page
        return state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)
            page?.prevKey?.plus(1, DateUnit.WEEK)
                ?: page?.nextKey?.minus(1, DateUnit.WEEK)
        }
    }

    override suspend fun load(params: LoadParams<Date>): LoadResult<Date, LogItemState> {
        val startDate = params.key ?: today
        val endDate = startDate.plus(1, DateUnit.WEEK).minus(1, DateUnit.DAY)

        val entries = entryRepository.getByDateRange(
            startDateTime = startDate.atStartOfDay(),
            endDateTime = endDate.atEndOfDay(),
        )

        val items = DateProgression(startDate, endDate).flatMap { date ->
            // FIXME: Gets added twice when prepending start of month
            val monthHeader = LogItemState.MonthHeader(
                dayState = LogDayState(
                    date = date,
                    dayOfMonthLocalized = formatter.formatDayOfMonth(date),
                    dayOfWeekLocalized = formatter.formatDayOfWeek(date, abbreviated = true),
                    style = LogDayStyle(isVisible = false, isHighlighted = false),
                ),
                dateLocalized = formatter.formatMonthOfYear(date.monthOfYear, abbreviated = false),
            )
            val headers = listOfNotNull(monthHeader.takeIf { date.dayOfMonth == 1 })
            val entriesOfDate = entries.filter { it.dateTime.date == date }
            val entryContent = entriesOfDate.takeIf(List<Entry>::isNotEmpty)?.map { entry ->
                LogItemState.EntryContent(
                    dayState = LogDayState(
                        date = date,
                        dayOfMonthLocalized = formatter.formatDayOfMonth(date),
                        dayOfWeekLocalized = formatter.formatDayOfWeek(date, abbreviated = true),
                        style = LogDayStyle(
                            isVisible = entry == entriesOfDate.first(),
                            isHighlighted = entry.dateTime.date == today,
                        ),
                    ),
                    entryState = mapEntryListItemState(entry, includeDate = false),
                )
            }
            val content = entryContent ?: listOf(
                LogItemState.EmptyContent(
                    dayState = LogDayState(
                        date = date,
                        dayOfMonthLocalized = formatter.formatDayOfMonth(date),
                        dayOfWeekLocalized = formatter.formatDayOfWeek(date, abbreviated = true),
                        style = LogDayStyle(
                            isVisible = true,
                            isHighlighted = date == today,
                        ),
                    ),
                ),
            )
            headers + content
        }

        val page = LoadResult.Page(
            data = items,
            prevKey = startDate.minus(1, DateUnit.WEEK),
            nextKey = endDate.plus(1, DateUnit.DAY),
        )
        return page
    }

    companion object {

        private const val PAGE_SIZE_IN_DAYS = DateTimeConstants.DAYS_PER_WEEK

        fun newConfig(): PagingConfig {
            return PagingConfig(
                pageSize = PAGE_SIZE_IN_DAYS,
                enablePlaceholders = true,
            )
        }
    }
}