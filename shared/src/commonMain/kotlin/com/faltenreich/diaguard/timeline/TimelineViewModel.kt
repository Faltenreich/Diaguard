package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.measurement.category.usecase.GetActiveMeasurementCategoriesWithPropertiesUseCase
import com.faltenreich.diaguard.measurement.value.usecase.GetMeasurementValuesInDateRangeUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.preference.color.ColorSchemePreference
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.timeline.canvas.chart.GetTimelineChartDataUseCase
import com.faltenreich.diaguard.timeline.canvas.table.GetTimelineTableDataUseCase
import com.faltenreich.diaguard.timeline.date.TimelineDateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class TimelineViewModel(
    getToday: GetTodayUseCase,
    formatDate: FormatTimelineDateUseCase,
    getPreference: GetPreferenceUseCase,
    getCategories: GetActiveMeasurementCategoriesWithPropertiesUseCase,
    private val getValues: GetMeasurementValuesInDateRangeUseCase,
    private val getChartData: GetTimelineChartDataUseCase,
    private val getTableData: GetTimelineTableDataUseCase,
    private val pushScreen: PushScreenUseCase,
) : ViewModel<TimelineState, TimelineIntent, TimelineEvent>() {

    private val initialDate = MutableStateFlow(getToday())
    private val currentDate = MutableStateFlow(initialDate.value)
    private val dateRange = currentDate.map { date ->
        date.minus(2, DateUnit.DAY) .. date.plus(2, DateUnit.DAY)
    }
    private val categoriesWithProperties = getCategories()
    private val categories = categoriesWithProperties.map { it.keys.toList() }
    private val properties = categoriesWithProperties.map { it.values.flatten() }
    private val values = dateRange.flatMapLatest(getValues::invoke)
    private val decimalPlaces = getPreference(DecimalPlacesPreference)
    private val chartData = combine(values, properties, getChartData::invoke)
    private val tableData = combine(values, properties, categories, decimalPlaces, getTableData::invoke)
    private val dateDialog = MutableStateFlow<TimelineState.DateDialog?>(null)
    private val date = combine(
        initialDate,
        currentDate,
        currentDate.map(formatDate::invoke),
        dateDialog,
        ::TimelineDateState,
    )

    override val state = combine(
        chartData,
        tableData,
        date,
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