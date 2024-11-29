package com.faltenreich.diaguard.entry.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.Time
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
import com.faltenreich.diaguard.navigation.modal.CloseModalUseCase
import com.faltenreich.diaguard.navigation.modal.OpenModalUseCase
import com.faltenreich.diaguard.navigation.screen.PopScreenUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.view.DeleteModal
import com.faltenreich.diaguard.tag.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EntryFormViewModel(
    entryId: Long? = null,
    dateTimeIsoString: String? = null,
    foodId: Long? = null,
    getEntryById: GetEntryByIdUseCase = inject(),
    private val getFoodById: GetFoodByIdUseCase = inject(),
    getDateTimeForEntry: GetDateTimeForEntryUseCase = inject(),
    getMeasurementCategoryInputState: GetMeasurementCategoryInputStateUseCase = inject(),
    getFoodEatenInputState: GetFoodEatenInputStateUseCase = inject(),
    getTagsOfEntry: GetTagsOfEntry = inject(),
    getTagsByQuery: GetTagsByQueryUseCase = inject(),
    private val popScreen: PopScreenUseCase = inject(),
    private val pushScreen: PushScreenUseCase = inject(),
    private val showModal: OpenModalUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
    private val validate: ValidateEntryFormInputUseCase = inject(),
    private val storeEntry: StoreEntryUseCase = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
    private val formatDateTime: FormatDateTimeUseCase = inject(),
) : ViewModel<EntryFormState, EntryFormIntent, Unit>() {

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

    var note: String by mutableStateOf(editing?.note ?: "")

    var alarmDelayInMinutes: Int? by mutableStateOf(null)

    private val measurements = MutableStateFlow((emptyList<MeasurementCategoryInputState>()))

    private val foodEaten = MutableStateFlow(listOfNotNull(food?.let(::FoodEatenInputState)))

    var tagQuery = MutableStateFlow("")
    var tagSelection = MutableStateFlow(emptyList<Tag>())
    private val tagSuggestions = combine(
        tagQuery,
        tagSelection,
    ) { tagQuery, tagsSelected -> tagQuery to tagsSelected }
        .flatMapLatest { (tagQuery, tagsSelected) ->
            getTagsByQuery(tagQuery, tagsSelected)
        }

    private val error = MutableStateFlow<String?>(null)

    override val state = combine(
        measurements,
        foodEaten,
        tagSuggestions,
        error,
        ::EntryFormState,
    )

    init {
        scope.launch {
            getMeasurementCategoryInputState(editing).collectLatest {
                measurements.value += it
            }
        }
        scope.launch {
            foodEaten.value += getFoodEatenInputState(editing)
        }
        scope.launch {
            tagSelection.value += getTagsOfEntry(editing)
        }
        scope.launch {
            combine(
                snapshotFlow { note },
                measurements,
                foodEaten,
                tagQuery,
            ) { /* Observe any user input */ }.collectLatest {
                error.update { null }
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
        measurements.update { measurements ->
            measurements.map { category ->
                category.copy(propertyInputStates = category.propertyInputStates.map { legacy ->
                    when (legacy.property) {
                        update.property -> update
                        else -> legacy
                    }
                })
            }
        }
    }

    private suspend fun selectDate() {
        showModal(
            DatePickerModal(
                date = date,
                onPick = {
                    date = it
                    scope.launch { closeModal() }
                },
            )
        )
    }

    private suspend fun selectTime() {
        showModal(
            TimePickerModal(
                time = time,
                onPick = {
                    time = it
                    scope.launch { closeModal() }
                },
            )
        )
    }

    private suspend fun submit() {
        val input = EntryFormInput(
            entry = editing,
            dateTime = dateTime,
            measurements = measurements.value,
            tags = tagSelection.value,
            note = note.takeIf(String::isNotBlank),
            foodEaten = foodEaten.value,
        )
        error.update { null }
        when (val result = validate(input)) {
            is ValidationResult.Success -> {
                storeEntry(input)
                popScreen()
            }
            is ValidationResult.Failure -> {
                measurements.value = result.data.measurements
                error.update { result.error }
            }
        }
    }

    private suspend fun delete() {
        val entry = editing
        if (entry != null) {
            showModal(
                DeleteModal(
                    onDismissRequest = { scope.launch { closeModal() } },
                    onConfirmRequest = {
                        deleteEntry(entry)
                        scope.launch {
                            closeModal()
                            popScreen()
                        }
                    },
                )
            )
        } else {
            popScreen()
        }
    }

    private suspend fun selectFood() {
        pushScreen(FoodSearchScreen(mode = FoodSearchMode.FIND))
    }

    private fun addFood(food: Food.Local) {
        foodEaten.update { foodEaten -> foodEaten + FoodEatenInputState(food) }
    }

    private fun editFood(food: FoodEatenInputState) {
        foodEaten.update { foodEaten ->
            foodEaten.map { legacy ->
                when (legacy.food) {
                    food.food -> food
                    else -> legacy
                }
            }
        }
    }

    private fun removeFood(food: FoodEatenInputState) {
        foodEaten.update { foodEaten -> foodEaten - food }
    }

    private fun addTag(tag: Tag) {
        tagSelection.value = tagSelection.value.plus(tag)
    }

    private fun removeTag(tag: Tag) {
        tagSelection.value = tagSelection.value.minus(tag)
    }
}