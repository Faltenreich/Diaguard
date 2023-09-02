package com.faltenreich.diaguard.entry.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.measurement.GetMeasurementsInputDataUseCase
import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInputData
import com.faltenreich.diaguard.entry.form.measurement.MeasurementTypeInputData
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class EntryFormViewModel(
    entry: Entry?,
    date: Date?,
    private val dispatcher: CoroutineDispatcher = inject(),
    private val submitEntry: SubmitEntryUseCase = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
    private val getMeasurementInputData: GetMeasurementsInputDataUseCase = inject(),
) : ViewModel() {

    private val id: Long? = entry?.id

    var dateTime: DateTime by mutableStateOf(entry?.dateTime ?: date?.atTime(DateTime.now().time) ?: DateTime.now())
        private set
    var date: Date
        get() = dateTime.date
        set(value) { dateTime = dateTime.time.atDate(value) }
    var time: Time
        get() = dateTime.time
        set(value) { dateTime = dateTime.date.atTime(value) }

    var tag: String by mutableStateOf("")
    var note: String by mutableStateOf(entry?.note ?: "")

    var measurements: List<MeasurementPropertyInputData> by mutableStateOf(emptyList())

    init {
        viewModelScope.launch(dispatcher) {
            measurements = getMeasurementInputData(entry)
        }
    }

    fun updateMeasurementValue(update: MeasurementTypeInputData) = viewModelScope.launch(dispatcher) {
        measurements = measurements.map { property ->
            property.copy(typeInputDataList = property.typeInputDataList.map { value ->
                when (value.type) {
                    update.type -> update
                    else -> value
                }
            })
        }
    }

    fun submit() = viewModelScope.launch(dispatcher) {
        submitEntry(
            id = id,
            dateTime = dateTime,
            note = note,
            measurements = measurements.flatMap(MeasurementPropertyInputData::typeInputDataList),
        )
    }

    fun deleteIfNeeded() = viewModelScope.launch(dispatcher) {
        // TODO: Intercept with confirmation dialog if something has changed
        val id = id ?: return@launch
        deleteEntry(id)
    }
}