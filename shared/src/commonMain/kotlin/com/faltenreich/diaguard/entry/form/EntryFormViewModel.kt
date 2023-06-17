package com.faltenreich.diaguard.entry.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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

    private val id: Long? = entry?.id

    val isEditing: Boolean
        get() = id != null

    var dateTime: DateTime by mutableStateOf(
        entry?.dateTime
            ?: date?.atTime(DateTime.now().time)
            ?: DateTime.now()
    )

    var note: String by mutableStateOf(entry?.note ?: "")

    private val measurementLegacy = getMeasurementsUseCase(entry?.id)
    private var measurementInput by mutableStateOf((emptyList<MeasurementInput>()))
    private val measurementInputFlow = snapshotFlow { measurementInput }
    private val measurements = combine(measurementLegacy, measurementInputFlow) { legacy, input ->
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

    private val state = measurements.map { measurements ->
        EntryFormViewState(measurements = measurements,)
    }.flowOn(dispatcher)

    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = EntryFormViewState(
            measurements = MeasurementInputViewState(properties = emptyList()),
        )
    )

    fun setDate(date: Date) = viewModelScope.launch(dispatcher) {
        dateTime = dateTime.time.atDate(date)
    }

    fun setTime(time: Time) = viewModelScope.launch(dispatcher) {
        dateTime = dateTime.date.atTime(time)
    }

    fun setMeasurement(type: MeasurementType, value: String) {
        measurementInput = measurementInput
            .filterNot { it.type == type }
            .plus(MeasurementInput(type = type, input = value))
    }

    fun submit() = viewModelScope.launch(dispatcher) {
        submitEntry(
            id = id,
            dateTime = dateTime,
            note = note,
            measurements = viewState.value.measurements,
        )
    }

    fun delete() = viewModelScope.launch(dispatcher) {
        val id = id ?: throw IllegalStateException("Cannot delete entry without id")
        deleteEntry(id)
    }
}