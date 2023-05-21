package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.measurement.GetMeasurementsUseCase
import com.faltenreich.diaguard.entry.form.measurement.MeasurementInput
import com.faltenreich.diaguard.entry.form.measurement.MeasurementInputViewState
import com.faltenreich.diaguard.measurement.type.MeasurementType
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
import kotlinx.coroutines.launch

class EntryFormViewModel(
    entry: Entry?,
    private val submitEntry: SubmitEntryUseCase = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
    getMeasurementsUseCase: GetMeasurementsUseCase = inject(),
) : ViewModel() {

    private val id = MutableStateFlow(entry?.id)
    private val dateTime = MutableStateFlow(entry?.dateTime ?: DateTime.now())
    private val note = MutableStateFlow(entry?.note)
    private val measurementLegacy = getMeasurementsUseCase(entry?.id)
    private val measurementInput = MutableStateFlow((emptyList<MeasurementInput>()))
    private val measurements = combine(measurementLegacy, measurementInput) { legacy, input ->
        legacy.copy(
            properties = legacy.properties.map { property ->
                property.copy(
                    values = property.values.map { value ->
                        value.copy(
                            input = input.firstOrNull { it.type == value.type }?.input ?: value.input
                        )
                    }
                )
            },
        )
    }

    private val state = combine(
        id,
        dateTime,
        note,
        measurements,
    ) { id, dateTime, note, measurements ->
        EntryFormViewState(
            dateTime = dateTime,
            note = note,
            isEditing = id != null,
            measurements = measurements,
        )
    }

    // TODO: If true, intercept back navigation via LocalNavigator.currentOrThrow
    private val hasChanged = state.distinctUntilChanged { old, new -> old != new }

    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = EntryFormViewState(
            dateTime = dateTime.value,
            note = note.value,
            isEditing = id.value != null,
            measurements = MeasurementInputViewState(properties = emptyList()),
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

    fun setMeasurement(value: String, type: MeasurementType) = viewModelScope.launch {
        measurementInput.value = measurementInput.value
            .filterNot { it.type == type }
            .plus(MeasurementInput(input = value, type = type))
    }

    fun submit() {
        submitEntry(
            id = id.value,
            dateTime = dateTime.value,
            note = note.value,
            measurements = viewState.value.measurements,
        )
    }

    fun delete() {
        val id = this.id.value ?: throw IllegalStateException("Cannot delete entry without id")
        deleteEntry(id)
    }
}