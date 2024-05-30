package com.faltenreich.diaguard.log

import androidx.compose.ui.unit.IntSize
import androidx.paging.cachedIn
import app.cash.paging.Pager
import app.cash.paging.PagingSource
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.log.item.InvalidateLogDayStickyHeaderInfoUseCase
import com.faltenreich.diaguard.log.item.LogDayStickyHeaderInfo
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.navigation.modal.DatePickerModal
import com.faltenreich.diaguard.navigation.screen.EntryFormScreen
import com.faltenreich.diaguard.navigation.screen.EntrySearchScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.logging.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class LogViewModel(
    getToday: GetTodayUseCase = inject(),
    private val invalidateStickyHeaderInfo: InvalidateLogDayStickyHeaderInfoUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val showModal: OpenModalUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
) : ViewModel<LogState, LogIntent, Unit>() {

    private val initialDate: Date = getToday()
    private lateinit var dataSource: PagingSource<Date, LogItem>
    private val currentDate = MutableStateFlow(initialDate)

    val pagingData = Pager(
        config = LogItemSource.newConfig(),
        initialKey = initialDate,
        pagingSourceFactory = { LogItemSource().also { dataSource = it } },
    ).flow.cachedIn(scope)

    private val monthHeaderSize = MutableStateFlow(IntSize.Zero)
    private val dayHeaderSize = MutableStateFlow(IntSize.Zero)
    private val stickyHeaderInfo = MutableStateFlow(LogDayStickyHeaderInfo())

    override val state: Flow<LogState> = combine(
        monthHeaderSize,
        dayHeaderSize,
        stickyHeaderInfo,
        ::LogState,
    )

    override suspend fun handleIntent(intent: LogIntent) {
        when (intent) {
            is LogIntent.CacheMonthHeaderSize -> monthHeaderSize.value = intent.size
            is LogIntent.CacheDayHeaderSize -> dayHeaderSize.value = intent.size
            is LogIntent.OnScroll -> {
                currentDate.value = intent.firstItem.date
                stickyHeaderInfo.value = invalidateStickyHeaderInfo(
                    stickyHeaderInfo = stickyHeaderInfo.value,
                    monthHeaderSize = monthHeaderSize.value,
                    dayHeaderSize = dayHeaderSize.value,
                    firstItem = intent.firstItem,
                    nextItems = intent.nextItems,
                )
            }
            is LogIntent.CreateEntry -> navigateToScreen(EntryFormScreen(date = intent.date))
            is LogIntent.OpenEntry -> navigateToScreen(EntryFormScreen(entry = intent.entry))
            is LogIntent.SearchEntries -> navigateToScreen(EntrySearchScreen())
            is LogIntent.SelectDate -> selectDate()
            is LogIntent.SetDate -> setDate(intent.date)
        }
    }

    private fun selectDate() {
        showModal(
            DatePickerModal(
                date = currentDate.value,
                onPick = {
                    dispatchIntent(LogIntent.SetDate(it))
                    closeModal()
                },
            )
        )
    }

    private fun setDate(date: Date) {
        Logger.debug("Set date: $date")
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