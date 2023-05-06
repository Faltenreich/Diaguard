package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.di.inject
import dev.icerock.moko.resources.StringResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class EntryFormViewModel(
    entry: Entry?,
    getEntry: GetEntryUseCase = inject(),
    private val submitEntry: SubmitEntryUseCase = inject(),
) : ViewModel() {

    private val state = MutableStateFlow(EntryFormViewState(getEntry(entry)))
    val viewState = state.asStateFlow()

    val title: StringResource = if (entry != null) MR.strings.entry_edit else MR.strings.entry_new

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
        submitEntry(state.value.entry)
    }
}