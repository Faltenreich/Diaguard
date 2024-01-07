package com.faltenreich.diaguard.entry.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.food.GetFoodEatenInputDataUseCase
import com.faltenreich.diaguard.entry.form.measurement.GetMeasurementsInputDataUseCase
import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInputData
import com.faltenreich.diaguard.entry.form.measurement.MeasurementTypeInputData
import com.faltenreich.diaguard.entry.form.tag.GetTagsByQueryUseCase
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEatenInputData
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.screen.FoodListScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.datetime.FormatDateTimeUseCase
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.tag.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EntryFormViewModel(
    entry: Entry?,
    date: Date?,
    dateTimeFactory: DateTimeFactory = inject(),
    getTagsByQuery: GetTagsByQueryUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val createEntry: CreateEntryUseCase = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
    private val getMeasurementInputData: GetMeasurementsInputDataUseCase = inject(),
    private val getFoodEatenInputData: GetFoodEatenInputDataUseCase = inject(),
    private val formatDateTime: FormatDateTimeUseCase = inject(),
) : ViewModel<EntryFormState, EntryFormIntent>() {

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

    var tag = MutableStateFlow("")

    var note: String by mutableStateOf(entry?.note ?: "")

    var alarmDelayInMinutes: Int? by mutableStateOf(null)

    var measurements by mutableStateOf(emptyList<MeasurementPropertyInputData>())
    var foodEaten by mutableStateOf(emptyList<FoodEatenInputData>())
    var tags by mutableStateOf(emptyList<Tag>())

    override val state: Flow<EntryFormState> = tag
        .debounce(DateTimeConstants.INPUT_DEBOUNCE)
        .flatMapLatest { query -> getTagsByQuery(query, tags).map(::EntryFormState) }

    init {
        scope.launch(Dispatchers.IO) {
            val measurements = getMeasurementInputData(entry)
            withContext(Dispatchers.Main) {
                this@EntryFormViewModel.measurements = measurements
            }
        }
        scope.launch(Dispatchers.IO) {
            getFoodEatenInputData(entry).collectLatest { foodEaten ->
                withContext(Dispatchers.Main) {
                    this@EntryFormViewModel.foodEaten = foodEaten
                }
            }
        }
    }

    override fun onIntent(intent: EntryFormIntent) {
        when (intent) {
            is EntryFormIntent.Edit -> updateMeasurementValue(intent.data)
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
        createEntry(
            id = id,
            dateTime = dateTime,
            note = note,
            measurements = measurements,
            foodEaten = foodEaten,
            tags = tags,
        )
        navigateBack()
    }

    private fun deleteIfNeeded() {
        // TODO: Intercept with confirmation dialog if something has changed
        val id = id ?: return
        deleteEntry(id)
        navigateBack()
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