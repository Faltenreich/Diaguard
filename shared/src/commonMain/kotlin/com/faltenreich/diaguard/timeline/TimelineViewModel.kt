package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.preference.color.ColorSchemePreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class TimelineViewModel(
    getToday: GetTodayUseCase,
    formatDate: FormatTimelineDateUseCase,
    getPreference: GetPreferenceUseCase,
    getData: GetTimelineDataUseCase,
    private val pushScreen: PushScreenUseCase,
) : ViewModel<TimelineState, TimelineIntent, TimelineEvent>() {

    private val initialDate = MutableStateFlow(getToday())
    private val currentDate = MutableStateFlow(initialDate.value)
    private val dateDialog = MutableStateFlow<TimelineState.DateDialog?>(null)
    private val data = currentDate.flatMapLatest(getData::invoke)

    override val state = com.faltenreich.diaguard.shared.architecture.combine(
        initialDate,
        currentDate,
        currentDate.map(formatDate::invoke),
        data,
        dateDialog,
        getPreference(ColorSchemePreference),
        ::TimelineState,
    )

    override suspend fun handleIntent(intent: TimelineIntent) {
        when (intent) {
            is TimelineIntent.CreateEntry -> pushScreen(EntryFormScreen())
            is TimelineIntent.SearchEntries -> pushScreen(EntrySearchScreen())
            is TimelineIntent.MoveDayBack -> selectDate(currentDate.value.minus(1, DateUnit.DAY))
            is TimelineIntent.MoveDayForward -> selectDate(currentDate.value.plus(1, DateUnit.DAY))
            is TimelineIntent.OpenDateDialog -> dateDialog.update { TimelineState.DateDialog(currentDate.value) }
            is TimelineIntent.CloseDateDialog -> dateDialog.update { null }
            is TimelineIntent.SetCurrentDate -> currentDate.update { intent.currentDate }
        }
    }

    private fun selectDate(date: Date) {
        postEvent(TimelineEvent.DateSelected(date))
    }
}