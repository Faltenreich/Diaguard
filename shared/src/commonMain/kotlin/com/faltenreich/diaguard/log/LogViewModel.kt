package com.faltenreich.diaguard.log

import androidx.compose.ui.unit.IntSize
import androidx.paging.cachedIn
import app.cash.paging.Pager
import app.cash.paging.PagingSource
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.datetime.picker.DatePickerModal
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.log.item.InvalidateLogDayStickyHeaderInfoUseCase
import com.faltenreich.diaguard.log.item.LogDayStickyHeaderInfo
import com.faltenreich.diaguard.log.item.LogItemState
import com.faltenreich.diaguard.navigation.modal.CloseModalUseCase
import com.faltenreich.diaguard.navigation.modal.OpenModalUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.logging.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class LogViewModel(
    getToday: GetTodayUseCase,
    private val invalidateStickyHeaderInfo: InvalidateLogDayStickyHeaderInfoUseCase,
    private val pushScreen: PushScreenUseCase,
    private val showModal: OpenModalUseCase,
    private val closeModal: CloseModalUseCase,
) : ViewModel<LogState, LogIntent, Unit>() {

    private val initialDate: Date = getToday()
    private lateinit var dataSource: PagingSource<Date, LogItemState>
    private val currentDate = MutableStateFlow(initialDate)

    val pagingData = Pager(
        config = LogPagingSource.newConfig(),
        initialKey = initialDate,
        pagingSourceFactory = { LogPagingSource().also { dataSource = it } },
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
        with(intent) {
            when (this) {
                is LogIntent.CacheMonthHeaderSize -> monthHeaderSize.value = size
                is LogIntent.CacheDayHeaderSize -> dayHeaderSize.value = size
                is LogIntent.OnScroll -> {
                    currentDate.value = firstItem.date
                    stickyHeaderInfo.value = invalidateStickyHeaderInfo(
                        stickyHeaderInfo = stickyHeaderInfo.value,
                        monthHeaderSize = monthHeaderSize.value,
                        dayHeaderSize = dayHeaderSize.value,
                        firstItem = firstItem,
                        nextItems = nextItems,
                    )
                }
                is LogIntent.CreateEntry -> pushScreen(EntryFormScreen(date = date))
                is LogIntent.OpenEntry -> pushScreen(EntryFormScreen(entry = entry))
                is LogIntent.OpenEntrySearch -> pushScreen(EntrySearchScreen(query))
                is LogIntent.SelectDate -> selectDate()
                is LogIntent.SetDate -> setDate(date)
            }
        }
    }

    private fun selectDate() = scope.launch {
        showModal(
            DatePickerModal(
                date = currentDate.value,
                onPick = {
                    dispatchIntent(LogIntent.SetDate(it))
                    scope.launch { closeModal() }
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