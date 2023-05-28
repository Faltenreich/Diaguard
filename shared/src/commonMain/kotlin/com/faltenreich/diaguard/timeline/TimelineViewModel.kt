package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.deep
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

class TimelineViewModel(
    initialDate: Date,
    dispatcher: CoroutineDispatcher = inject(),
    entryRepository: EntryRepository = inject(),
) : ViewModel() {

    private val currentDate = MutableStateFlow(initialDate)
    private val entries = entryRepository.getAll().deep()
    private val state = combine(currentDate, entries) { currentDate, entries ->
        TimelineViewState.Responding(currentDate, entries)
    }.flowOn(dispatcher)
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = TimelineViewState.Requesting(initialDate),
    )

    fun setDate(date: Date) {
        currentDate.value = date
    }
}