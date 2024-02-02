package com.faltenreich.diaguard.log

import app.cash.paging.Pager
import app.cash.paging.PagingSource
import com.faltenreich.diaguard.entry.form.DeleteEntryUseCase
import com.faltenreich.diaguard.log.item.InvalidateLogDayStickyHeaderInfoUseCase
import com.faltenreich.diaguard.log.item.LogDayStickyHeaderInfo
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.screen.EntryFormScreen
import com.faltenreich.diaguard.navigation.screen.EntrySearchScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.GetTodayUseCase
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class LogViewModel(
    date: Date?,
    getToday: GetTodayUseCase = inject(),
    private val invalidateStickyHeaderInfo: InvalidateLogDayStickyHeaderInfoUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
) : ViewModel<LogState, LogIntent>() {

    private val initialDate: Date = date ?: getToday()
    private lateinit var dataSource: PagingSource<Date, LogItem>
    val currentDate = MutableStateFlow(initialDate)

    val pagingData = Pager(
        config = LogItemSource.newConfig(),
        initialKey = initialDate,
        pagingSourceFactory = { LogItemSource().also { dataSource = it } },
    ).flow.cachedIn(scope)

    private val monthHeaderHeight = MutableStateFlow(0)
    private val dayHeaderHeight = MutableStateFlow(0)
    private val stickyDayInfo = MutableStateFlow(LogDayStickyHeaderInfo())

    override val state: Flow<LogState> = combine(
        monthHeaderHeight,
        dayHeaderHeight,
        stickyDayInfo,
        ::LogState,
    )

    override fun onIntent(intent: LogIntent) {
        when (intent) {
            is LogIntent.SetMonthHeaderHeight -> monthHeaderHeight.value = intent.monthHeaderHeight
            is LogIntent.SetDayHeaderHeight -> dayHeaderHeight.value = intent.dayHeaderHeight
            is LogIntent.InvalidateStickyHeaderInfo -> stickyDayInfo.value = invalidateStickyHeaderInfo(
                stickyHeaderInfo = stickyDayInfo.value,
                monthHeaderHeight = monthHeaderHeight.value,
                dayHeaderHeight = dayHeaderHeight.value,
                firstItem = intent.firstItem,
                nextItems = intent.nextItems,
            )
            is LogIntent.CreateEntry -> navigateToScreen(EntryFormScreen(date = intent.date))
            is LogIntent.OpenEntry -> navigateToScreen(EntryFormScreen(entry = intent.entry))
            is LogIntent.SearchEntries -> navigateToScreen(EntrySearchScreen())
            is LogIntent.SetDate -> setDate(intent.date)
            is LogIntent.Remove -> {
                deleteEntry(intent.item.entry.id)
                dataSource.invalidate()
            }
        }
    }

    fun setDate(date: Date) {
        // TODO
        /*
        val indexOfDate = items.first().indexOfFirst { it.date == date }
        if (indexOfDate >= 0) {
            pagination.value = pagination.value.copy(targetDate = date)
        } else {
            val startDate = date.minusMonths(1)
            val endDate = date.plusMonths(1)
            Logger.debug("LogViewModel: Start date is $startDate, end date is $endDate")
            pagination.value = pagination.value.copy(
                minimumDate = startDate,
                maximumDate = endDate,
                targetDate = date,
            )
        }
        */
    }
}