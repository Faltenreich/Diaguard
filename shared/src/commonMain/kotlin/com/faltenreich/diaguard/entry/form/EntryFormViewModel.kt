package com.faltenreich.diaguard.entry.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.Time
import com.faltenreich.diaguard.datetime.factory.DateTimeConstants
import com.faltenreich.diaguard.datetime.format.FormatDateTimeUseCase
import com.faltenreich.diaguard.datetime.picker.DatePickerModal
import com.faltenreich.diaguard.datetime.picker.TimePickerModal
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.datetime.GetDateTimeForEntryUseCase
import com.faltenreich.diaguard.entry.form.food.GetFoodEatenInputStateUseCase
import com.faltenreich.diaguard.entry.form.measurement.GetMeasurementCategoryInputStateUseCase
import com.faltenreich.diaguard.entry.form.measurement.MeasurementCategoryInputState
import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInputState
import com.faltenreich.diaguard.entry.form.measurement.ValidateEntryFormInputUseCase
import com.faltenreich.diaguard.entry.form.tag.GetTagsByQueryUseCase
import com.faltenreich.diaguard.entry.form.tag.GetTagsOfEntry
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEatenInputState
import com.faltenreich.diaguard.food.search.FoodSearchMode
import com.faltenreich.diaguard.food.search.FoodSearchScreen
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.navigation.ShowSnackbarUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.view.DeleteModal
import com.faltenreich.diaguard.tag.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EntryFormViewModel(
    private val entryId: Long? = null,
    dateTimeIsoString: String? = null,
    foodId: Long? = null,
    getEntryById: GetEntryByIdUseCase = inject(),
    getFoodById: GetFoodByIdUseCase = inject(),
    getDateTimeForEntry: GetDateTimeForEntryUseCase = inject(),
    getMeasurementCategoryInputState: GetMeasurementCategoryInputStateUseCase = inject(),
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
    private val deleteEntry: DeleteEntryUseCase = inject(),
    private val formatDateTime: FormatDateTimeUseCase = inject(),
) : ViewModel<EntryFormState, EntryFormIntent, Unit>() {

    constructor(screen: EntryFormScreen) : this(
        entryId = screen.entryId,
        dateTimeIsoString = screen.dateTimeIsoString,
        foodId = screen.foodId,
    )

    private val editing: Entry.Local? = entryId?.let(getEntryById::invoke)
    private val food: Food.Local? = foodId?.let(getFoodById::invoke)

    var dateTime: DateTime by mutableStateOf(getDateTimeForEntry(editing, dateTimeIsoString))
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

    // TODO: Reconsider rememberSaveable to avoid persisting state after returning to Composable
    var note: String by mutableStateOf(editing?.note ?: "")

    var alarmDelayInMinutes: Int? by mutableStateOf(null)

    var measurements by mutableStateOf(emptyList<MeasurementCategoryInputState>())

    var foodEaten by mutableStateOf(listOfNotNull(food?.let(::FoodEatenInputState)))

    var tagQuery = MutableStateFlow("")
    var tagSelection = MutableStateFlow(emptyList<Tag>())
    private val tagSuggestions = combine(
        tagQuery.debounce(DateTimeConstants.INPUT_DEBOUNCE),
        tagSelection,
    ) { tagQuery, tagsSelected -> tagQuery to tagsSelected }
        .flatMapLatest { (tagQuery, tagsSelected) ->
            getTagsByQuery(tagQuery, tagsSelected)
        }

    override val state: Flow<EntryFormState> = tagSuggestions.map(::EntryFormState)

    init {
        scope.launch {
            getMeasurementCategoryInputState(editing).collectLatest { measurements ->
                this@EntryFormViewModel.measurements += measurements
            }
        }
        scope.launch(Dispatchers.IO) {
            val foodEaten = getFoodEatenInputState(editing)
            withContext(Dispatchers.Main) {
                this@EntryFormViewModel.foodEaten += foodEaten
            }
        }
        scope.launch(Dispatchers.IO) {
            val tagsOfEntry = getTagsOfEntry(editing)
            withContext(Dispatchers.Main) {
                this@EntryFormViewModel.tagSelection.value += tagsOfEntry
            }
        }
    }

    override suspend fun handleIntent(intent: EntryFormIntent): Unit = with(intent) {
        when (this) {
            is EntryFormIntent.Edit -> edit(data)
            is EntryFormIntent.SelectDate -> selectDate()
            is EntryFormIntent.SelectTime -> selectTime()
            is EntryFormIntent.Submit -> submit()
            is EntryFormIntent.Delete -> delete()
            is EntryFormIntent.SelectFood -> selectFood()
            is EntryFormIntent.AddFood -> addFood(food)
            is EntryFormIntent.EditFood -> editFood(food)
            is EntryFormIntent.RemoveFood -> removeFood(food)
            is EntryFormIntent.AddTag -> addTag(tag)
            is EntryFormIntent.RemoveTag -> removeTag(tag)
        }
    }

    private fun edit(update: MeasurementPropertyInputState) {
        measurements = measurements.map { category ->
            category.copy(propertyInputStates = category.propertyInputStates.map { legacy ->
                when (legacy.property) {
                    update.property -> update
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
            id = entryId,
            dateTime = dateTime,
            measurements = measurements,
            tags = tagSelection.value,
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

    private fun delete() {
        val entry = editing
        if (entry != null) {
            showModal(
                DeleteModal(
                    onDismissRequest = closeModal::invoke,
                    onConfirmRequest = {
                        deleteEntry(entry)
                        closeModal()
                        navigateBack()
                    },
                )
            )
        } else {
            navigateBack()
        }
    }

    private suspend fun selectFood() {
        navigateToScreen(FoodSearchScreen(mode = FoodSearchMode.FIND))
    }

    private fun addFood(food: Food.Local) {
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

    // FIXME: Does not work after adding new food to an existing entry
    private fun removeFood(food: FoodEatenInputState) {
        foodEaten -= food
    }

    private fun addTag(tag: Tag) {
        tagSelection.value = tagSelection.value.plus(tag)
    }

    private fun removeTag(tag: Tag) {
        tagSelection.value = tagSelection.value.minus(tag)
    }
}