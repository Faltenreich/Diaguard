package com.faltenreich.diaguard.entry.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.alarm.AlarmDelay
import com.faltenreich.diaguard.entry.form.food.GetFoodEatenInputDataUseCase
import com.faltenreich.diaguard.entry.form.measurement.GetMeasurementsInputDataUseCase
import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInputData
import com.faltenreich.diaguard.entry.form.measurement.MeasurementTypeInputData
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEatenInputData
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.datetime.FormatDateTimeUseCase
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EntryFormViewModel(
    entry: Entry?,
    date: Date?,
    dateTimeFactory: DateTimeFactory = inject(),
    private val dispatcher: CoroutineDispatcher = inject(),
    private val submitEntry: SubmitEntryUseCase = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
    private val getMeasurementInputData: GetMeasurementsInputDataUseCase = inject(),
    private val getFoodEatenInputData: GetFoodEatenInputDataUseCase = inject(),
    private val formatDateTime: FormatDateTimeUseCase = inject(),
    private val localization: Localization = inject(),
) : ViewModel() {

    private val id: Long? = entry?.id

    var dateTime: DateTime by mutableStateOf(entry?.dateTime
        ?: date?.atTime(dateTimeFactory.now().time)
        ?: dateTimeFactory.now()
    )
        private set

    var date: Date
        get() = dateTime.date
        set(value) { dateTime = dateTime.time.atDate(value) }
    val dateFormatted: String
        get() = formatDateTime(date)

    var time: Time
        get() = dateTime.time
        set(value) { dateTime = dateTime.date.atTime(value) }
    val timeFormatted: String
        get() = formatDateTime(time)

    var tag: String by mutableStateOf("")

    var note: String by mutableStateOf(entry?.note ?: "")

    var alarmDelay: AlarmDelay by mutableStateOf(AlarmDelay.None)
    val alarmDelayFormatted: String
        get() = alarmDelay.minutes?.let { minutes ->
            localization.getString(MR.strings.alarm_placeholder, minutes)
        } ?: localization.getString(MR.strings.alarm_none)

    var measurements by mutableStateOf(emptyList<MeasurementPropertyInputData>())
    var foodEaten by mutableStateOf(emptyList<FoodEatenInputData>())

    init {
        // Attention: Switching Dispatcher fixes
        // IllegalStateException: Reading a state that was created after the
        // snapshot was taken or in a snapshot that has not yet been applied
        // TODO: Fix underlying problem by switching to StateFlow
        viewModelScope.launch(Dispatchers.IO) {
            measurements = getMeasurementInputData(entry)
        }
        viewModelScope.launch(Dispatchers.IO) {
            getFoodEatenInputData(entry).collectLatest {
                foodEaten = it
            }
        }
    }

    fun handleIntent(intent: EntryFormIntent) = viewModelScope.launch(dispatcher) {
        when (intent) {
            is EntryFormIntent.Edit -> updateMeasurementValue(intent.data)
            is EntryFormIntent.Submit -> submit()
            is EntryFormIntent.Delete -> deleteIfNeeded()
            is EntryFormIntent.AddFood -> addFood(intent.food)
            is EntryFormIntent.EditFood -> editFood(intent.food)
            is EntryFormIntent.RemoveFood -> removeFood(intent.food)
        }
    }

    private fun updateMeasurementValue(update: MeasurementTypeInputData) {
        measurements = measurements.map { property ->
            property.copy(typeInputDataList = property.typeInputDataList.map { legacy ->
                when (legacy.type) {
                    update.type -> update
                    else -> legacy
                }
            })
        }
    }

    private fun submit() {
        submitEntry(
            id = id,
            dateTime = dateTime,
            note = note,
            measurements = measurements.flatMap(MeasurementPropertyInputData::typeInputDataList),
            foodEaten = foodEaten,
        )
    }

    private fun deleteIfNeeded() {
        // TODO: Intercept with confirmation dialog if something has changed
        val id = id ?: return
        deleteEntry(id)
    }

    private fun addFood(food: Food) {
        foodEaten += FoodEatenInputData(food)
    }

    private fun editFood(food: FoodEatenInputData) {
        foodEaten = foodEaten.map { legacy ->
            when (legacy.food) {
                food.food -> food
                else -> legacy
            }
        }
    }

    private fun removeFood(food: FoodEatenInputData) {
        foodEaten -= food
    }
}