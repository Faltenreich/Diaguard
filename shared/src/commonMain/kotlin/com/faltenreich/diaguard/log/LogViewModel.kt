package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.DeleteEntryUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.di.inject
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
    private val state = combine(currentDate, data) { currentDate, data ->
        LogViewState(currentDate, data)
    }.flowOn(dispatcher)
    val viewState: StateFlow<LogViewState> = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = LogViewState(initialDate, emptyList()),
    )

    init {
        viewModelScope.launch(dispatcher) {
            val endDate = initialDate.plusMonths(1)
            data.value = data.value + getLogData(startDate = initialDate, endDate = endDate)
            pagination.value = pagination.value.copy(maximumDate = endDate)
        }
    }

    fun setDate(date: Date) {
        currentDate.value = date
    }

    fun delete(entry: Entry) {
        deleteEntry(entry.id)
    }

    fun nextMonth() = viewModelScope.launch(dispatcher) {
        val startDate = pagination.value.maximumDate.plusDays(1)
        val endDate = startDate.plusMonths(1)
        data.value = data.value + getLogData(startDate = startDate, endDate = endDate)
        pagination.value = pagination.value.copy(maximumDate = endDate)
    }
}