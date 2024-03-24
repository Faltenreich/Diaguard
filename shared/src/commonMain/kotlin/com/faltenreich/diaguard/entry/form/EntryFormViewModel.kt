package com.faltenreich.diaguard.entry.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.datetime.GetDateTimeForEntryUseCase
import com.faltenreich.diaguard.entry.form.food.GetFoodEatenInputStateUseCase
import com.faltenreich.diaguard.entry.form.measurement.GetMeasurementPropertyInputStateUseCase
import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInputState
import com.faltenreich.diaguard.entry.form.measurement.MeasurementTypeInputState
import com.faltenreich.diaguard.entry.form.measurement.ValidateEntryFormInputUseCase
import com.faltenreich.diaguard.entry.form.tag.GetTagsByQueryUseCase
import com.faltenreich.diaguard.entry.form.tag.GetTagsOfEntry
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEatenInputState
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.navigation.ShowSnackbarUseCase
import com.faltenreich.diaguard.navigation.modal.DatePickerModal
import com.faltenreich.diaguard.navigation.modal.EntryDeleteModal
import com.faltenreich.diaguard.navigation.modal.TimePickerModal
import com.faltenreich.diaguard.navigation.screen.FoodListScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.datetime.FormatDateTimeUseCase
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.tag.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EntryFormViewModel(
    entry: Entry?,
    date: Date?,
    getDateTimeForEntry: GetDateTimeForEntryUseCase = inject(),
    getMeasurementPropertyInputState: GetMeasurementPropertyInputStateUseCase = inject(),
    getFoodEatenInputState: GetFoodEatenInputStateUseCase = inject(),
    getTagsOfEntry: GetTagsOfEntry = inject(),
    getTagsByQuery: GetTagsByQueryUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val showModal: OpenModalUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
    private val showSnackbar: ShowSnackbarUseCase = inject(),
    private val validate: ValidateEntryFormInputUseCase = inject(),
    private val createEntry: CreateEntryUseCase = inject(),
    private val formatDateTime: FormatDateTimeUseCase = inject(),
) : ViewModel<EntryFormState, EntryFormIntent>() {

    private val editing: Entry? = entry
    private val id: Long? = entry?.id

    var dateTime: DateTime by mutableStateOf(getDateTimeForEntry(entry, date))
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

    var tag = MutableStateFlow("")

    var note: String by mutableStateOf(entry?.note ?: "")

    var alarmDelayInMinutes: Int? by mutableStateOf(null)

    var measurements by mutableStateOf(emptyList<MeasurementPropertyInputState>())
    var foodEaten by mutableStateOf(emptyList<FoodEatenInputState>())
    var tags by mutableStateOf(emptyList<Tag>())

    override val state: Flow<EntryFormState> = tag
        .debounce(DateTimeConstants.INPUT_DEBOUNCE)
        .flatMapLatest { query -> getTagsByQuery(query, tags).map(::EntryFormState) }

    init {
        scope.launch(Dispatchers.IO) {
            val measurements = getMeasurementPropertyInputState(entry)
            withContext(Dispatchers.Main) {
                this@EntryFormViewModel.measurements = measurements
            }
        }
        scope.launch(Dispatchers.IO) {
            val foodEaten = getFoodEatenInputState(entry)
            withContext(Dispatchers.Main) {
                this@EntryFormViewModel.foodEaten = foodEaten
            }
        }
        scope.launch(Dispatchers.IO) {
            val tags = getTagsOfEntry(entry)
            withContext(Dispatchers.Main) {
                this@EntryFormViewModel.tags = tags
            }
        }
    }

    override fun onIntent(intent: EntryFormIntent) {
        when (intent) {
            is EntryFormIntent.Edit -> updateMeasurementValue(intent.data)
            is EntryFormIntent.SelectDate -> selectDate()
            is EntryFormIntent.SelectTime -> selectTime()
            is EntryFormIntent.Submit -> submit()
            is EntryFormIntent.Delete -> deleteIfNeeded()
            is EntryFormIntent.SelectFood -> navigateToScreen(FoodListScreen(
                onSelection = { food -> dispatchIntent(EntryFormIntent.AddFood(food)) }),
            )
            is EntryFormIntent.AddFood -> addFood(intent.food)
            is EntryFormIntent.EditFood -> editFood(intent.food)
            is EntryFormIntent.RemoveFood -> removeFood(intent.food)
            is EntryFormIntent.AddTag -> tags += intent.tag // TODO: Refresh suggestions via getTagsByQuery()
            is EntryFormIntent.RemoveTag -> tags -= intent.tag
        }
    }

    private fun updateMeasurementValue(update: MeasurementTypeInputState) {
        measurements = measurements.map { property ->
            property.copy(typeInputStates = property.typeInputStates.map { legacy ->
                when (legacy.type) {
                    update.type -> update
                    else -> legacy
                }
            })
        }
    }

    private fun selectDate() {
        showModal(
            DatePickerModal(
                date = date,
                onPick = {
                    date = it
                    closeModal()
                },
            )
        )
    }

    private fun selectTime() {
        showModal(
            TimePickerModal(
                time = time,
                onPick = {
                    time = it
                    closeModal()
                },
            )
        )
    }

    private fun submit() = scope.launch {
        val input = EntryFormInput(
            id = id,
            dateTime = dateTime,
            measurements = measurements,
            tags = tags,
            note = note.takeIf(String::isNotBlank),
            foodEaten = foodEaten,
        )
        when (val result = validate(input)) {
            is ValidationResult.Success -> {
                createEntry(input)
                navigateBack()
            }
            is ValidationResult.Failure -> {
                measurements = result.data.measurements
                showSnackbar(message = result.error)
            }
        }
    }

    private fun deleteIfNeeded() {
        val entry = editing ?: return
        showModal(EntryDeleteModal(entry = entry))
        // TODO: navigateBack()
    }

    private fun addFood(food: Food) {
        foodEaten += FoodEatenInputState(food)
    }

    private fun editFood(food: FoodEatenInputState) {
        foodEaten = foodEaten.map { legacy ->
            when (legacy.food) {
                food.food -> food
                else -> legacy
            }
        }
    }

    private fun removeFood(food: FoodEatenInputState) {
        foodEaten -= food
    }
}