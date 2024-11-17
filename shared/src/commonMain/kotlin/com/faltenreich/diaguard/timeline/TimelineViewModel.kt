package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.datetime.picker.DatePickerModal
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.measurement.category.GetActiveMeasurementCategoriesUseCase
import com.faltenreich.diaguard.navigation.modal.CloseModalUseCase
import com.faltenreich.diaguard.navigation.modal.OpenModalUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TimelineViewModel(
    getToday: GetTodayUseCase,
    formatDate: FormatTimelineDateUseCase,
    getCategories: GetActiveMeasurementCategoriesUseCase,
    getPreference: GetPreferenceUseCase,
    getValues: GetMeasurementValuesAroundDateUseCase,
    getData: GetTimelineDataUseCase,
    private val pushScreen: PushScreenUseCase,
    private val showModal: OpenModalUseCase,
    private val closeModal: CloseModalUseCase,
) : ViewModel<TimelineState, TimelineIntent, TimelineEvent>() {

    private val initialDate = MutableStateFlow(getToday())
    private val currentDate = MutableStateFlow(initialDate.value)
    private val categories = getCategories()
    private val values = currentDate.flatMapLatest(getValues::invoke)
    private val decimalPlaces = getPreference(DecimalPlacesPreference)
    private val data = combine(categories, values, decimalPlaces, getData::invoke)

    override val state = combine(
        initialDate,
        currentDate,
        currentDate.map(formatDate::invoke),
        data,
        ::TimelineState,
    )

    override suspend fun handleIntent(intent: TimelineIntent) {
        when (intent) {
            is TimelineIntent.CreateEntry -> pushScreen(EntryFormScreen())
            is TimelineIntent.SearchEntries -> pushScreen(EntrySearchScreen())
            is TimelineIntent.ShowDatePicker -> showDatePicker()
            is TimelineIntent.MoveDayBack -> selectDate(currentDate.value.minus(1, DateUnit.DAY))
            is TimelineIntent.MoveDayForward -> selectDate(currentDate.value.plus(1, DateUnit.DAY))
            is TimelineIntent.SetCurrentDate -> currentDate.value = intent.currentDate
        }
    }

    private suspend fun showDatePicker() {
        showModal(
            DatePickerModal(
                date = currentDate.value,
                onPick = { date ->
                    selectDate(date)
                    scope.launch { closeModal() }
                },
            )
        )
    }

    private fun selectDate(date: Date) {
        postEvent(TimelineEvent.DateSelected(date))
    }
}