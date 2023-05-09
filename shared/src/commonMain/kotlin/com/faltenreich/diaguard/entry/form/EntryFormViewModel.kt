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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class EntryFormViewModel(
    entry: Entry?,
    private val submitEntry: SubmitEntryUseCase = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
) : ViewModel() {

    private val id = MutableStateFlow(entry?.id)
    private val dateTime = MutableStateFlow(entry?.dateTime ?: DateTime.now())
    private val note = MutableStateFlow(entry?.note)

    private val state = combine(dateTime, note, ::EntryFormViewState)
    val viewState = state.stateIn(viewModelScope, SharingStarted.Lazily, EntryFormViewState(dateTime.value, note.value))

    val title: StringResource
        get() =
            if (id.value != null) MR.strings.entry_edit
            else MR.strings.entry_new

    fun setDate(date: Date) {
        this.dateTime.value = dateTime.value.time.atDate(date)
    }

    fun setTime(time: Time) {
        this.dateTime.value = dateTime.value.date.atTime(time)
    }

    fun setNote(note: String) {
        this.note.value = note
    }

    fun submit() {
        // submitEntry(state.value.entry)
    }

    fun delete() {
        val id = this.id.value ?: throw IllegalStateException("Cannot delete entry without id")
        deleteEntry(id)
    }
}