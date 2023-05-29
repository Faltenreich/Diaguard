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
    private val getLogItems: GetLogItemsUseCase = inject(),
    private val mapLogItems: MapLogItemsUseCase = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
) : ViewModel() {

    private data class LogPaginationState(
        val minimumDate: Date,
        val maximumDate: Date,
        val targetDate: Date? = null,
    )

    private val currentDate = MutableStateFlow(initialDate)
    private val pagination = MutableStateFlow(LogPaginationState(minimumDate = initialDate, maximumDate = initialDate))
    private val items = MutableStateFlow(emptyList<LogItem>())
    private val state = combine(currentDate, pagination, items) { currentDate, pagination, items ->
        val scrollPosition = pagination.targetDate?.let { date -> items.indexOfFirst { it.date == date } }
        LogViewState(
            currentDate = currentDate,
            items = mapLogItems(items),
            scrollPosition = scrollPosition,
        )
    }.flowOn(dispatcher)
    val viewState: StateFlow<LogViewState> = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = LogViewState(initialDate, emptyMap()),
    )

    init {
        setDate(initialDate)
    }

    fun setDate(date: Date) = viewModelScope.launch(dispatcher) {
        val indexOfDate = items.value.indexOfFirst { it.date == date }
        if (indexOfDate >= 0) {
            pagination.value = pagination.value.copy(targetDate = date)
        } else {
            val startDate = date.minusMonths(1)
            val endDate = date.plusMonths(1)
            val initialItems = getLogItems(startDate = startDate, endDate = endDate)
            println("Setting initial items between $startDate and $endDate: $initialItems")
            items.value = items.value + initialItems
            pagination.value = pagination.value.copy(
                minimumDate = startDate,
                maximumDate = endDate,
                targetDate = date,
            )
        }
    }

    private fun previousMonth() = viewModelScope.launch(dispatcher) {
        val endDate = pagination.value.minimumDate.minusDays(1)
        val startDate = endDate.minusMonths(1)
        val previousItems = getLogItems(startDate = startDate, endDate = endDate)
        println("Setting previous items between $startDate and $endDate: $previousItems")
        items.value = previousItems + items.value
        pagination.value = pagination.value.copy(minimumDate = startDate, targetDate = null)
    }

    private fun nextMonth() = viewModelScope.launch(dispatcher) {
        val startDate = pagination.value.maximumDate.plusDays(1)
        val endDate = startDate.plusMonths(1)
        val nextItems = getLogItems(startDate = startDate, endDate = endDate)
        println("Setting next items between $startDate and $endDate: $nextItems")
        items.value = items.value + nextItems
        pagination.value = pagination.value.copy(maximumDate = endDate, targetDate = null)
    }

    fun onScroll(firstVisibleItemIndex: Int) = viewModelScope.launch(dispatcher) {
        val firstVisibleItem = items.value.getOrNull(firstVisibleItemIndex) ?: return@launch
        currentDate.value = firstVisibleItem.date
    }

    fun resetScroll() = viewModelScope.launch {
        pagination.value = pagination.value.copy(targetDate = null)
    }

    fun onPagination(direction: PaginationDirection) {
        when (direction) {
            PaginationDirection.START -> previousMonth()
            PaginationDirection.END -> nextMonth()
        }
    }

    fun remove(item: LogItem.EntryContent) = viewModelScope.launch(dispatcher) {
        items.value = items.value.filterNot { it == item }
        deleteEntry(item.entry.id)
    }
}