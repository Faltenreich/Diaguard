package com.faltenreich.diaguard.log

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.faltenreich.diaguard.entry.form.DeleteEntryUseCase
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.log.usecase.GetLogItemsUseCase
import com.faltenreich.diaguard.log.usecase.LogItemPagingSource
import com.faltenreich.diaguard.log.usecase.MapLogItemsUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.pagination.PaginationDirection
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LogViewModel(
    initialDate: Date,
    private val dispatcher: CoroutineDispatcher = inject(),
    getLogItems: GetLogItemsUseCase = inject(),
    private val mapLogItems: MapLogItemsUseCase = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
) : ViewModel() {

    val currentDate = MutableStateFlow(initialDate)
    private val pagination = MutableStateFlow(
        LogPaginationState(
            minimumDate = initialDate.minusMonths(1),
            maximumDate = initialDate.plusMonths(1),
        )
    )
    // private val items: Flow<List<LogItem>> = getLogItems(pagination)
    val items: Flow<PagingData<LogItem>> = Pager(
        pagingSourceFactory = { LogItemPagingSource(initialDate = initialDate) },
        config = PagingConfig(pageSize = 20),
    ).flow
    private val state = combine(pagination, items) { pagination, items ->
        LogViewState(
            items = items,
            scrollPosition = null, // TODO: pagination.targetDate?.let { date -> items.indexOfFirst { it.date == date } }
        )
    }.flowOn(dispatcher)
    val viewState: StateFlow<LogViewState> = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = LogViewState(PagingData.empty<LogItem>()),
    )

    fun setDate(date: Date) = viewModelScope.launch(dispatcher) {
        val indexOfDate = -1 // TODO: items.first().indexOfFirst { it.date == date }
        if (indexOfDate >= 0) {
            pagination.value = pagination.value.copy(targetDate = date)
        } else {
            val startDate = date.minusMonths(1)
            val endDate = date.plusMonths(1)
            println("LogViewModel: Start date is $startDate, end date is $endDate")
            pagination.value = pagination.value.copy(
                minimumDate = startDate,
                maximumDate = endDate,
                targetDate = date,
            )
        }
    }

    private fun previousMonth() = viewModelScope.launch(dispatcher) {
        val startDate = pagination.value.minimumDate.minusMonths(1)
        println("LogViewModel: Start date is now $startDate")
        pagination.value = pagination.value.copy(minimumDate = startDate, targetDate = null)
    }

    private fun nextMonth() = viewModelScope.launch(dispatcher) {
        val endDate = pagination.value.maximumDate.plusMonths(1)
        println("LogViewModel: End date is now $endDate")
        pagination.value = pagination.value.copy(maximumDate = endDate, targetDate = null)
    }

    /*
    fun onScroll(firstVisibleItemIndex: Int) = viewModelScope.launch(dispatcher) {
        println("LogViewModel: onScroll")
        val firstVisibleItem = items.first().getOrNull(firstVisibleItemIndex) ?: return@launch
        currentDate.value = firstVisibleItem.date
    }
    */

    fun resetScroll() = viewModelScope.launch {
        println("LogViewModel: resetScroll")
        pagination.value = pagination.value.copy(targetDate = null)
    }

    fun onPagination(direction: PaginationDirection) {
        println("LogViewModel: Paging $direction")
        when (direction) {
            PaginationDirection.START -> previousMonth()
            PaginationDirection.END -> nextMonth()
        }
    }

    fun remove(item: LogItem.EntryContent) = viewModelScope.launch(dispatcher) {
        deleteEntry(item.entry.id)
    }
}