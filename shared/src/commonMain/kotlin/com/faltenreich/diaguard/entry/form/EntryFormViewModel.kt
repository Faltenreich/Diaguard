package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.datetime.format.FormatDateTimeUseCase
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.datetime.GetDateTimeForEntryUseCase
import com.faltenreich.diaguard.entry.form.food.GetFoodEatenInputStateUseCase
import com.faltenreich.diaguard.entry.form.measurement.GetMeasurementCategoryInputStateUseCase
import com.faltenreich.diaguard.entry.form.measurement.MeasurementCategoryInputState
import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInputState
import com.faltenreich.diaguard.entry.form.measurement.ValidateEntryFormInputUseCase
import com.faltenreich.diaguard.entry.form.reminder.GetReminderLabelUseCase
import com.faltenreich.diaguard.entry.form.reminder.SetReminderUseCase
import com.faltenreich.diaguard.entry.form.tag.GetTagSuggestionsUseCase
import com.faltenreich.diaguard.entry.form.tag.GetTagsOfEntry
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEatenInputState
import com.faltenreich.diaguard.food.search.FoodSearchMode
import com.faltenreich.diaguard.food.search.FoodSearchScreen
import com.faltenreich.diaguard.navigation.screen.PopScreenUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.navigation.system.OpenPermissionSettingsUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.logging.Logger
import com.faltenreich.diaguard.shared.permission.Permission
import com.faltenreich.diaguard.shared.permission.PermissionResult
import com.faltenreich.diaguard.shared.permission.RequestPermissionUseCase
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.list.GetTagsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class EntryFormViewModel(
    entryId: Long? = null,
    dateTimeIsoString: String? = null,
    foodId: Long? = null,
    getEntryById: GetEntryByIdUseCase = inject(),
    private val getFoodById: GetFoodByIdUseCase = inject(),
    private val getFoodEatenInputState: GetFoodEatenInputStateUseCase = inject(),
    getMeasurementCategoryInputState: GetMeasurementCategoryInputStateUseCase = inject(),
    getTags: GetTagsUseCase = inject(),
    getTagsOfEntry: GetTagsOfEntry = inject(),
    getTagSuggestions: GetTagSuggestionsUseCase = inject(),
    getDateTimeForEntry: GetDateTimeForEntryUseCase = inject(),
    private val popScreen: PopScreenUseCase = inject(),
    private val pushScreen: PushScreenUseCase = inject(),
    private val validate: ValidateEntryFormInputUseCase = inject(),
    private val storeEntry: StoreEntryUseCase = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
    private val setReminder: SetReminderUseCase = inject(),
    private val requestPermission: RequestPermissionUseCase = inject(),
    private val formatDateTime: FormatDateTimeUseCase = inject(),
    private val openPermissionSettings: OpenPermissionSettingsUseCase = inject(),
    private val getReminderLabel: GetReminderLabelUseCase = inject(),
) : ViewModel<EntryFormState, EntryFormIntent, Unit>() {

    private val editing: Entry.Local? = entryId?.let(getEntryById::invoke)
    private val food: Food.Local? = foodId?.let(getFoodById::invoke)

    private val dateTime = MutableStateFlow(getDateTimeForEntry(editing, dateTimeIsoString))

    private val tagSelection = MutableStateFlow(emptySet<Tag>())
    private val tagQuery = MutableStateFlow("")
    private val tagSuggestions = combine(
        getTags(),
        tagSelection,
        tagQuery,
        getTagSuggestions::invoke,
    )
    private val tags = combine(
        tagQuery,
        tagSuggestions,
        tagSelection,
        EntryFormState::Tags,
    )

    private val note = MutableStateFlow(editing?.note ?: "")

    private val reminderDelayInMinutes = MutableStateFlow<Int?>(null)
    private val reminder = combine(
        reminderDelayInMinutes,
        reminderDelayInMinutes.map(getReminderLabel::invoke),
        EntryFormState::Reminder,
    )

    private val measurements = MutableStateFlow((emptyList<MeasurementCategoryInputState>()))
    private val foodEaten = MutableStateFlow(emptyList<FoodEatenInputState>())

    private val deleteDialog = MutableStateFlow<EntryFormState.DeleteDialog?>(null)

    override val state = combine(
        dateTime.map { dateTime ->
            EntryFormState.DateTime(
                date = dateTime.date,
                dateLocalized = formatDateTime(dateTime.date),
                time = dateTime.time,
                timeLocalized = formatDateTime(dateTime.time),
            )
        },
        note,
        measurements,
        foodEaten,
        tags,
        reminder,
        deleteDialog,
        ::EntryFormState,
    )

    init {
        scope.launch {
            getMeasurementCategoryInputState(editing).collectLatest {
                measurements.value = it
            }
        }
        scope.launch {
            getFoodEatenInputState(entry = editing, food = food).collectLatest {
                foodEaten.value = it
            }
        }
        scope.launch {
            val entry = editing ?: return@launch
            getTagsOfEntry(entry).collectLatest { tags ->
                tagSelection.value = tags.toSet()
            }
        }
    }

    override suspend fun handleIntent(intent: EntryFormIntent) {
        when (intent) {
            is EntryFormIntent.SetDate -> dateTime.update { it.time.atDate(intent.date) }
            is EntryFormIntent.SetTime -> dateTime.update { it.date.atTime(intent.time) }
            is EntryFormIntent.SetNote -> note.update { intent.note }
            is EntryFormIntent.SetTagQuery -> tagQuery.update { intent.tagQuery }
            is EntryFormIntent.SetReminder -> reminderDelayInMinutes.update { intent.delayInMinutes }
            is EntryFormIntent.OpenReminderPicker -> openReminderPicker()
            is EntryFormIntent.Edit -> edit(intent.data)
            is EntryFormIntent.Submit -> submit()
            is EntryFormIntent.Delete -> delete(intent.needsConfirmation)
            is EntryFormIntent.CloseDeleteDialog -> deleteDialog.update { null }
            is EntryFormIntent.SelectFood -> selectFood()
            is EntryFormIntent.AddFood -> addFood(intent.food)
            is EntryFormIntent.EditFood -> editFood(intent.food)
            is EntryFormIntent.RemoveFood -> removeFood(intent.food)
            is EntryFormIntent.AddTag -> addTag(intent.tag)
            is EntryFormIntent.RemoveTag -> removeTag(intent.tag)
        }
    }

    private fun edit(update: MeasurementPropertyInputState) {
        measurements.update { measurements ->
            measurements.map { category ->
                category.copy(
                    propertyInputStates = category.propertyInputStates.map { legacy ->
                        when (legacy.property) {
                            update.property -> update.copy(
                                // Reset error on changing input
                                error = null,
                            )
                            else -> legacy
                        }
                    },
                )
            }
        }
    }

    private suspend fun openReminderPicker() {
        when (val result = requestPermission(Permission.POST_NOTIFICATIONS)) {
            is PermissionResult.Granted -> Unit
            is PermissionResult.Denied -> Unit
            is PermissionResult.Unknown -> Unit
        }
    }

    private suspend fun submit() {
        reminderDelayInMinutes.value?.minutes?.let { delay ->
            Logger.debug("Setting reminder in $delay")
            setReminder(delay)
        }

        val missingTag = tagQuery.value.takeIf(String::isNotBlank)?.let { Tag.User(it) }
        val input = EntryFormInput(
            entry = editing,
            dateTime = dateTime.value,
            measurements = measurements.value,
            tags = tagSelection.value + setOfNotNull(missingTag),
            note = note.value.takeIf(String::isNotBlank),
            foodEaten = foodEaten.value,
        )
        when (val result = validate(input)) {
            is ValidationResult.Success -> {
                storeEntry(input)
                popScreen()
            }
            is ValidationResult.Failure -> {
                measurements.value = result.data.measurements
            }
        }
    }

    private suspend fun delete(needsConfirmation: Boolean) {
        val entry = editing
        if (entry != null) {
            if (needsConfirmation) {
                deleteDialog.update { EntryFormState.DeleteDialog }
            } else {
                deleteEntry(entry)
                popScreen()
            }
        } else {
            popScreen()
        }
    }

    private suspend fun selectFood() {
        pushScreen(FoodSearchScreen(mode = FoodSearchMode.FIND))
    }

    private suspend fun addFood(food: Food.Local) {
        getFoodEatenInputState(food).collectLatest {
            foodEaten.update { foodEaten -> foodEaten + it }
        }
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