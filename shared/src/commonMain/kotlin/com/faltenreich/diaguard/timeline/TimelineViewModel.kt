package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.navigation.modal.DatePickerModal
import com.faltenreich.diaguard.navigation.screen.EntryFormScreen
import com.faltenreich.diaguard.navigation.screen.EntrySearchScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class TimelineViewModel(
    getToday: GetTodayUseCase = inject(),
    formatDate: FormatTimelineDateUseCase = inject(),
    getData: GetTimelineDataUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val showModal: OpenModalUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
) : ViewModel<TimelineState, TimelineIntent, TimelineEvent>() {

    private val initialDate = MutableStateFlow(getToday())
    private val currentDate = MutableStateFlow(initialDate.value)
    private val data = currentDate.flatMapLatest(getData::invoke)

    override val state = combine(
        initialDate,
        currentDate.map(formatDate::invoke),
        data,
        ::TimelineState,
    )

    override fun handleIntent(intent: TimelineIntent) {
        when (intent) {
            is TimelineIntent.CreateEntry -> navigateToScreen(EntryFormScreen())
            is TimelineIntent.SearchEntries -> navigateToScreen(EntrySearchScreen())
            is TimelineIntent.ShowDatePicker -> showDatePicker()
            is TimelineIntent.MoveDayBack -> selectDate(currentDate.value.minus(1, DateUnit.DAY))
            is TimelineIntent.MoveDayForward -> selectDate(currentDate.value.plus(1, DateUnit.DAY))
            is TimelineIntent.SetCurrentDate -> currentDate.value = intent.currentDate
        }
    }

    private fun showDatePicker() {
        showModal(
            DatePickerModal(
                date = currentDate.value,
                onPick = { date ->
                    selectDate(date)
                    closeModal()
                },
            )
        )
    }

    private fun selectDate(date: Date) {
        initialDate.value = date
        currentDate.value = date
        postEvent(TimelineEvent.SelectedDate)
    }
}