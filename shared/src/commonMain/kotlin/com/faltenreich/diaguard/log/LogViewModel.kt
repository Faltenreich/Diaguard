package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.entry.form.DeleteEntryUseCase
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.log.usecase.GetLogItemsUseCase
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
import kotlinx.coroutines.flow.first
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

    data class LogPaginationState(
        val minimumDate: Date,
        val maximumDate: Date,
        val targetDate: Date? = null,
    )

    val currentDate = MutableStateFlow(initialDate)
    private val pagination = MutableStateFlow(
        LogPaginationState(
            minimumDate = initialDate.minusMonths(1),
            maximumDate = initialDate.plusMonths(1),
        )
    )
    // FIXME: getLogItems is only called once
    private val items: Flow<List<LogItem>> = getLogItems(pagination)
    private val state = combine(pagination, items) { pagination, items ->
        val scrollPosition = pagination.targetDate?.let { date -> items.indexOfFirst { it.date == date } }
        LogViewState(
            items = mapLogItems(items),
            scrollPosition = scrollPosition,
        )
    }.flowOn(dispatcher)
    val viewState: StateFlow<LogViewState> = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = LogViewState(emptyMap()),
    )

    fun setDate(date: Date) = viewModelScope.launch(dispatcher) {
        val indexOfDate = items.first().indexOfFirst { it.date == date }
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

    fun onScroll(firstVisibleItemIndex: Int) = viewModelScope.launch(dispatcher) {
        println("LogViewModel: onScroll")
        val firstVisibleItem = items.first().getOrNull(firstVisibleItemIndex) ?: return@launch
        currentDate.value = firstVisibleItem.date
    }

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