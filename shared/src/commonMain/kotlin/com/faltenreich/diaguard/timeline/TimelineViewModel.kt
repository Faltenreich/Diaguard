package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class TimelineViewModel(
    initialDate: Date,
    entryRepository: EntryRepository = inject(),
) : ViewModel() {

    private val currentDate: MutableStateFlow<Date> = MutableStateFlow(initialDate)
    private val entries: Flow<List<Entry>> = entryRepository.getAll()
    private val state: Flow<TimelineViewState> = combine(currentDate, entries) { currentDate, entries -> TimelineViewState.Responding(currentDate, entries) }
    val viewState: StateFlow<TimelineViewState> = state.stateIn(viewModelScope, SharingStarted.Lazily, TimelineViewState.Requesting(initialDate))

    fun setDate(date: Date) {
        currentDate.value = date
    }
}