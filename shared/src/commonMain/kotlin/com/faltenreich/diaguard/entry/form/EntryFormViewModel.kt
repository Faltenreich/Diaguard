package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.DateTimeRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.Single

@Single
class EntryFormViewModel(
    private val entryRepository: EntryRepository = inject(),
    dateTimeRepository: DateTimeRepository = inject(),
) : ViewModel() {

    private val state = MutableStateFlow(EntryFormViewState(Entry(id = null, dateTime = dateTimeRepository.now())))
    val viewState = state.asStateFlow()

    fun setNote(note: String) {
        state.value = state.value.copy(entry = state.value.entry.copy(note = note))
    }

    fun submit() {
        entryRepository.insert(state.value.entry)
    }
}