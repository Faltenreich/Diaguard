package com.faltenreich.diaguard.entry.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EntryFormViewModel(
    entry: Entry?,
    date: Date?,
    private val dispatcher: CoroutineDispatcher = inject(),
    private val submitEntry: SubmitEntryUseCase = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
    getMeasurementsUseCase: GetMeasurementsUseCase = inject(),
) : ViewModel() {

    private val id = MutableStateFlow(entry?.id)

    private val dateTime = MutableStateFlow(
        entry?.dateTime
            ?: date?.atTime(DateTime.now().time)
            ?: DateTime.now()
    )

    var note by mutableStateOf(entry?.note)
        private set

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
        measurements,
    ) { id, dateTime, measurements ->
        EntryFormViewState(
            dateTime = dateTime,
            isEditing = id != null,
            measurements = measurements,
        )
    }.flowOn(dispatcher)

    // TODO: If true, intercept back navigation via LocalNavigator.currentOrThrow
    private val hasChanged = state.distinctUntilChanged { old, new -> old != new }

    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = EntryFormViewState(
            dateTime = dateTime.value,
            isEditing = id.value != null,
            measurements = MeasurementInputViewState(properties = emptyList()),
        )
    )

    fun setDate(date: Date) = viewModelScope.launch(dispatcher) {
        dateTime.value = dateTime.value.time.atDate(date)
    }

    fun setTime(time: Time) = viewModelScope.launch(dispatcher) {
        dateTime.value = dateTime.value.date.atTime(time)
    }

    fun updateNote(note: String) {
        this.note = note
    }

    fun setMeasurement(type: MeasurementType, value: String) = viewModelScope.launch(dispatcher) {
        measurementInput.value = measurementInput.value
            .filterNot { it.type == type }
            .plus(MeasurementInput(type = type, input = value))
    }

    fun submit() = viewModelScope.launch(dispatcher) {
        submitEntry(
            id = id.value,
            dateTime = dateTime.value,
            note = note,
            measurements = viewState.value.measurements,
        )
    }

    fun delete() = viewModelScope.launch(dispatcher) {
        val id = id.value ?: throw IllegalStateException("Cannot delete entry without id")
        deleteEntry(id)
    }
}