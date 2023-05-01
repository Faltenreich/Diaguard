package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class EntryFormViewModel(
    entry: Entry?,
) : ViewModel() {

    private val entryRepository: EntryRepository = inject()
    private val state = MutableStateFlow(EntryFormViewState(entry ?: entryRepository.create()))
    val viewState = state.asStateFlow()

    fun setDate(date: Date) {
        setDateTime(state.value.entry.dateTime.time.atDate(date))
    }

    fun setTime(time: Time) {
        setDateTime(state.value.entry.dateTime.date.atTime(time))
    }

    private fun setDateTime(dateTime: DateTime) {
        state.value = state.value.copy(entry = state.value.entry.copy(dateTime = dateTime))
    }

    fun setNote(note: String) {
        state.value = state.value.copy(entry = state.value.entry.copy(note = note))
    }

    fun submit() {
        entryRepository.update(state.value.entry)
    }
}