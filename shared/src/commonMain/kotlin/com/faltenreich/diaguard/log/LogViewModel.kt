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
    dispatcher: CoroutineDispatcher = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
) : ViewModel() {

    private val currentDate = MutableStateFlow(initialDate)
    private val pagination = MutableStateFlow(LogPaginationState())
    private val data = MutableStateFlow(emptyList<LogData>())
    private val state = combine(currentDate, data) { currentDate, data ->
        when {
            data.isEmpty() -> LogViewState.Requesting(initialDate)
            else -> LogViewState.Responding(currentDate, data)
        }
    }.flowOn(dispatcher)
    val viewState: StateFlow<LogViewState> = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = LogViewState.Requesting(initialDate),
    )

    init {
        viewModelScope.launch(dispatcher) {

        }
    }

    fun setDate(date: Date) {
        currentDate.value = date
    }

    fun delete(entry: Entry) {
        deleteEntry(entry.id)
    }
}