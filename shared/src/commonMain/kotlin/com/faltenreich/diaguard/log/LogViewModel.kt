package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.DeleteEntryUseCase
import com.faltenreich.diaguard.log.item.LogData
import com.faltenreich.diaguard.log.usecase.FetchEntriesUseCase
import com.faltenreich.diaguard.log.usecase.GetLogDataUseCase
import com.faltenreich.diaguard.log.usecase.MapLogDataUseCase
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
    private val getLogData: GetLogDataUseCase = inject(),
    private val fetchEntries: FetchEntriesUseCase = inject(),
    private val mapLogData: MapLogDataUseCase = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
) : ViewModel() {

    private data class LogPaginationState(
        val minimumDate: Date,
        val maximumDate: Date,
        val targetDate: Date? = null,
    )

    private val currentDate = MutableStateFlow(initialDate)
    private val pagination = MutableStateFlow(LogPaginationState(minimumDate = initialDate, maximumDate = initialDate))
    private val data = MutableStateFlow(emptyList<LogData>())
    private val state = combine(currentDate, pagination, data) { currentDate, pagination, data ->
        val scrollPosition = pagination.targetDate?.let { date -> data.indexOfFirst { it.date == date } }
        LogViewState(
            currentDate = currentDate,
            data = mapLogData(data),
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
        val indexOfDate = data.value.indexOfFirst { it.date == date }
        if (indexOfDate >= 0) {
            pagination.value = pagination.value.copy(targetDate = date)
        } else {
            val startDate = date.minusMonths(1)
            val endDate = date.plusMonths(1)
            data.value = data.value + getLogData(startDate = startDate, endDate = endDate)
            pagination.value = pagination.value.copy(
                minimumDate = startDate,
                maximumDate = endDate,
                targetDate = date,
            )
            requestEntries(startDate, endDate)
        }
    }

    private fun previousMonth() = viewModelScope.launch(dispatcher) {
        val endDate = pagination.value.minimumDate.minusDays(1)
        val startDate = endDate.minusMonths(1)
        data.value = getLogData(startDate = startDate, endDate = endDate) + data.value
        pagination.value = pagination.value.copy(minimumDate = startDate, targetDate = null)
        requestEntries(startDate, endDate)
    }

    private fun nextMonth() = viewModelScope.launch(dispatcher) {
        val startDate = pagination.value.maximumDate.plusDays(1)
        val endDate = startDate.plusMonths(1)
        data.value = data.value + getLogData(startDate = startDate, endDate = endDate)
        pagination.value = pagination.value.copy(maximumDate = endDate, targetDate = null)
        requestEntries(startDate, endDate)
    }

    private fun requestEntries(startDate: Date, endDate: Date) = viewModelScope.launch(dispatcher) {
        // TODO: Refactor and optimize this mess
        val entries = fetchEntries(startDate = startDate, endDate = endDate)
        data.value = data.value
            .map { it to entries[it.date] }
            .map {
                it.takeIf { it.first is LogData.EmptyContent }?.second?.firstOrNull()?.let { entry -> LogData.EntryContent(entry) }
                    ?: it.first
            }
    }

    fun onScroll(firstVisibleItemIndex: Int) = viewModelScope.launch(dispatcher) {
        currentDate.value = data.value[firstVisibleItemIndex].date
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

    fun delete(entry: Entry) {
        deleteEntry(entry.id)
    }
}