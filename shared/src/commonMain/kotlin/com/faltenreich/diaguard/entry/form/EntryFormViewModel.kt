package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.architecture.either.ValidationResult
import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.food.Food
import com.faltenreich.diaguard.data.navigation.NavigationTarget
import com.faltenreich.diaguard.data.tag.Tag
import com.faltenreich.diaguard.datetime.format.FormatDateTimeUseCase
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
import com.faltenreich.diaguard.food.eaten.FoodEatenInputState
import com.faltenreich.diaguard.injection.inject
import com.faltenreich.diaguard.logging.Logger
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.NavigateToUseCase
import com.faltenreich.diaguard.system.permission.HasPermissionUseCase
import com.faltenreich.diaguard.system.permission.Permission
import com.faltenreich.diaguard.system.permission.PermissionResult
import com.faltenreich.diaguard.system.permission.RequestPermissionUseCase
import com.faltenreich.diaguard.tag.list.GetTagsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration

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
    private val navigateTo: NavigateToUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
    private val validate: ValidateEntryFormInputUseCase = inject(),
    private val storeEntry: StoreEntryUseCase = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
    private val setReminder: SetReminderUseCase = inject(),
    private val getReminderLabel: GetReminderLabelUseCase = inject(),
    private val hasPermission: HasPermissionUseCase = inject(),
    private val requestPermission: RequestPermissionUseCase = inject(),
    private val formatDateTime: FormatDateTimeUseCase = inject(),
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

    private val reminderDuration = MutableStateFlow<Duration>(Duration.ZERO)
    private val reminderPicker = MutableStateFlow<EntryFormState.Reminder.Picker?>(null)
    private val reminder = combine(
        reminderDuration,
        reminderDuration.map(getReminderLabel::invoke),
        reminderPicker,
        EntryFormState::Reminder,
    )

    private val measurements = MutableStateFlow((emptyList<MeasurementCategoryInputState>()))
    private val foodEaten = MutableStateFlow(emptyList<FoodEatenInputState>())

    private val deleteDialog = MutableStateFlow<EntryFormState.DeleteDialog?>(null)

    override val state = combine(
        dateTime.map { dateTime ->
            EntryFormState.DateTime(
                date = dateTime.date,
                dateLocalized = formatDateTime.formatDate(dateTime.date),
                time = dateTime.time,
                timeLocalized = formatDateTime.formatTime(dateTime.time),
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
            is EntryFormIntent.SetReminder -> reminderDuration.update { intent.duration }
            is EntryFormIntent.OpenReminderPicker -> openReminderPicker()
            is EntryFormIntent.CloseReminderPicker -> reminderPicker.update { null }
            is EntryFormIntent.RequestNotificationPermission -> requestNotificationPermission()
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
        reminderPicker.update {
            EntryFormState.Reminder.Picker(
                duration = reminderDuration.value,
                isPermissionGranted = hasPermission(Permission.POST_NOTIFICATIONS),
            )
        }
    }

    private suspend fun requestNotificationPermission() {
        when (requestPermission(Permission.POST_NOTIFICATIONS)) {
            is PermissionResult.Granted -> reminderPicker.update {
                EntryFormState.Reminder.Picker(
                    duration = reminderDuration.value,
                    isPermissionGranted = true,
                )
            }

            is PermissionResult.Denied -> reminderPicker.update { null }
            is PermissionResult.Unknown -> reminderPicker.update { null }
        }
    }

    private suspend fun submit() {
        reminderDuration.value.takeIf { it > Duration.ZERO }?.let { duration ->
            Logger.debug("Setting reminder in $duration")
            setReminder(duration)
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
                navigateBack()
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
                navigateBack()
            }
        } else {
            navigateBack()
        }
    }

    private suspend fun selectFood() {
        navigateTo(NavigationTarget.FoodSearch(mode = NavigationTarget.FoodSearch.Mode.FIND))
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