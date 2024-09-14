package com.faltenreich.diaguard.log

import app.cash.paging.PagingConfig
import app.cash.paging.PagingSource
import app.cash.paging.PagingSourceLoadParams
import app.cash.paging.PagingSourceLoadResult
import app.cash.paging.PagingSourceLoadResultPage
import app.cash.paging.PagingState
import com.faltenreich.diaguard.datetime.DateProgression
import com.faltenreich.diaguard.datetime.MonthOfYear
import com.faltenreich.diaguard.datetime.MonthYearUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.log.item.LogDayStyle
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.di.inject

class LogItemSource(
    getTodayUseCase: GetTodayUseCase = inject(),
    private val entryRepository: EntryRepository = inject(),
    private val valueRepository: MeasurementValueRepository = inject(),
    private val entryTagRepository: EntryTagRepository = inject(),
    private val foodEatenRepository: FoodEatenRepository = inject(),
) : PagingSource<MonthOfYear, LogItem>() {

    private val today = getTodayUseCase()

    override fun getRefreshKey(state: PagingState<MonthOfYear, LogItem>): MonthOfYear? {
        return state.closestItemToPosition(state.anchorPosition ?: 0)?.date?.monthOfYear
    }

    override suspend fun load(
        params: PagingSourceLoadParams<MonthOfYear>,
    ): PagingSourceLoadResult<MonthOfYear, LogItem> {
        val monthOfYear = params.key ?: today.monthOfYear

        val startDate = monthOfYear.firstDay
        val endDate = monthOfYear.lastDay

        val entries = entryRepository.getByDateRange(
            startDateTime = startDate.atStartOfDay(),
            endDateTime = endDate.atEndOfDay(),
        ).map { entry ->
            entry.apply {
                values = valueRepository.getByEntryId(entry.id)
                entryTags = entryTagRepository.getByEntryId(entry.id)
                foodEaten = foodEatenRepository.getByEntryId(entry.id)
            }
        }

        val items = DateProgression(startDate, endDate).map { date ->
            val headers = listOfNotNull(LogItem.MonthHeader(date).takeIf { date.dayOfMonth == 1 })
            val entriesOfDate = entries.filter { it.dateTime.date == date }
            val entryContent = entriesOfDate.takeIf(List<Entry>::isNotEmpty)?.map { entry ->
                LogItem.EntryContent(
                    entry = entry,
                    style = LogDayStyle(
                        isVisible = entry == entriesOfDate.first(),
                        isHighlighted = entry.dateTime.date == today,
                    ),
                )
            }
            val content = entryContent ?: listOf(
                LogItem.EmptyContent(
                    date = date,
                    style = LogDayStyle(
                        isVisible = true,
                        isHighlighted = date == today,
                    ),
                ),
            )
            headers + content
        }.flatten()

        return PagingSourceLoadResultPage(
            data = items,
            prevKey = monthOfYear.minus(1, unit = MonthYearUnit.MONTH),
            nextKey = monthOfYear.plus(1, unit = MonthYearUnit.MONTH),
        )
    }

    companion object {

        // Must be at least the maximum day count per month to ensure a prepending header
        private const val PAGE_SIZE_IN_DAYS = 31

        fun newConfig(): PagingConfig {
            return PagingConfig(pageSize = PAGE_SIZE_IN_DAYS)
        }
    }
}