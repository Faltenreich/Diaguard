package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.DeleteEntryUseCase
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
    private val deleteEntry: DeleteEntryUseCase = inject(),
) : ViewModel() {

    private val currentDate = MutableStateFlow(initialDate)
    private val pagination = MutableStateFlow(LogPaginationState(minimumDate = initialDate, maximumDate = initialDate))
    private val data = MutableStateFlow(emptyList<LogData>())
    private val state = combine(pagination, data) { pagination, data ->
        val scrollPosition = pagination.targetDate?.let { targetDate -> data.indexOfFirst { it.date == targetDate } }
        LogViewState(
            currentDate = currentDate.value,
            data = data,
            scrollPosition = scrollPosition,
        )
    }.flowOn(dispatcher)
    val viewState: StateFlow<LogViewState> = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = LogViewState(initialDate, emptyList()),
    )

    init {
        setDate(initialDate)
    }

    fun setDate(date: Date) = viewModelScope.launch(dispatcher) {
        val startDate = date.minusMonths(1)
        val endDate = date.plusMonths(1)
        data.value = data.value + getLogData(startDate = startDate, endDate = endDate)
        pagination.value = pagination.value.copy(
            minimumDate = startDate,
            maximumDate = endDate,
            targetDate = date,
        )
    }

    fun delete(entry: Entry) {
        deleteEntry(entry.id)
    }

    fun onPagination(direction: PaginationDirection) {
        when (direction) {
            PaginationDirection.START -> previousMonth()
            PaginationDirection.END -> nextMonth()
        }
    }

    private fun previousMonth() = viewModelScope.launch(dispatcher) {
        val endDate = pagination.value.minimumDate.minusDays(1)
        val startDate = endDate.minusMonths(1)
        data.value = getLogData(startDate = startDate, endDate = endDate) + data.value
        pagination.value = pagination.value.copy(minimumDate = startDate, targetDate = null)
    }

    private fun nextMonth() = viewModelScope.launch(dispatcher) {
        val startDate = pagination.value.maximumDate.plusDays(1)
        val endDate = startDate.plusMonths(1)
        data.value = data.value + getLogData(startDate = startDate, endDate = endDate)
        pagination.value = pagination.value.copy(maximumDate = endDate, targetDate = null)
    }
}