package com.faltenreich.diaguard.log

import androidx.compose.ui.unit.IntSize
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.log.list.LogListPagingSource
import com.faltenreich.diaguard.log.list.item.LogDayStickyInfo
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class LogViewModel(
    getToday: GetTodayUseCase,
    private val invalidateDayStickyInfo: InvalidateLogDayStickyInfoUseCase,
    private val pushScreen: PushScreenUseCase,
) : ViewModel<LogState, LogIntent, Unit>() {

    private val initialDate = MutableStateFlow(getToday())
    private val currentDate = MutableStateFlow(initialDate.value)

    private val monthHeaderSize = MutableStateFlow(IntSize.Zero)
    private val dayHeaderSize = MutableStateFlow(IntSize.Zero)
    private val dayStickyInfo = MutableStateFlow(LogDayStickyInfo())
    private val datePickerDialog = MutableStateFlow<LogState.DatePickerDialog?>(null)

    override val state = combine(
        initialDate.map { initialDate ->
            Pager(
                config = LogListPagingSource.newConfig(),
                initialKey = initialDate,
                pagingSourceFactory = { LogListPagingSource() },
            ).flow.cachedIn(scope)
        },
        monthHeaderSize,
        dayHeaderSize,
        dayStickyInfo,
        datePickerDialog,
        ::LogState,
    )

    override suspend fun handleIntent(intent: LogIntent) {
        with(intent) {
            when (this) {
                is LogIntent.CacheMonthHeaderSize -> monthHeaderSize.value = size
                is LogIntent.CacheDayHeaderSize -> dayHeaderSize.value = size
                is LogIntent.OnScroll -> {
                    currentDate.value = firstItem.date
                    dayStickyInfo.value = invalidateDayStickyInfo(
                        stickyHeaderInfo = dayStickyInfo.value,
                        monthHeaderSize = monthHeaderSize.value,
                        dayHeaderSize = dayHeaderSize.value,
                        firstItem = firstItem,
                        nextItems = nextItems,
                    )
                }
                is LogIntent.CreateEntry -> pushScreen(EntryFormScreen(date = date))
                is LogIntent.OpenEntry -> pushScreen(EntryFormScreen(entry = entry))
                is LogIntent.OpenEntrySearch -> pushScreen(EntrySearchScreen(query))
                is LogIntent.OpenDatePickerDialog ->
                    datePickerDialog.update { LogState.DatePickerDialog(currentDate.value) }
                is LogIntent.CloseDatePickerDialog -> datePickerDialog.update { null }
                is LogIntent.SetDate -> setDate(date)
            }
        }
    }

    private fun setDate(date: Date) {
        initialDate.update { date }
        // FIXME: Month header hides given date, so we should skip offset from month header
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