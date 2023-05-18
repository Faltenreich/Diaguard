package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.measurement.GetMeasurementDataUseCase
import com.faltenreich.diaguard.entry.form.measurement.MeasurementData
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn

class EntryFormViewModel(
    entry: Entry?,
    private val submitEntry: SubmitEntryUseCase = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
    getMeasurementData: GetMeasurementDataUseCase = inject(),
) : ViewModel() {

    private val id = MutableStateFlow(entry?.id)
    private val dateTime = MutableStateFlow(entry?.dateTime ?: DateTime.now())
    private val note = MutableStateFlow(entry?.note)

    // TODO: If true, intercept back navigation via LocalNavigator.currentOrThrow
    private val hasChanged = note.distinctUntilChanged { old, new -> old != new }

    private val state = combine(
        id,
        dateTime,
        note,
        getMeasurementData(),
    ) { id, dateTime, note, measurementData ->
        EntryFormViewState(
            dateTime = dateTime,
            note = note,
            isEditing = id != null,
            measurementData = measurementData,
        )
    }
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = EntryFormViewState(
            dateTime = dateTime.value,
            note = note.value,
            isEditing = id.value != null,
            measurementData = MeasurementData(properties = emptyList()),
        )
    )

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
        submitEntry(
            id = id.value,
            dateTime = dateTime.value,
            note = note.value,
        )
    }

    fun delete() {
        val id = this.id.value ?: throw IllegalStateException("Cannot delete entry without id")
        deleteEntry(id)
    }
}