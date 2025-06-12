package com.faltenreich.diaguard.timeline

import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvasDimensions
import com.faltenreich.diaguard.timeline.canvas.chart.GetTimelineChartMeasurementPropertyUseCase
import com.faltenreich.diaguard.timeline.canvas.chart.GetTimelineChartMeasurementValuesUseCase
import com.faltenreich.diaguard.timeline.canvas.chart.GetTimelineChartStateUseCase
import com.faltenreich.diaguard.timeline.canvas.table.GetTimelineTableMeasurementPropertiesUseCase
import com.faltenreich.diaguard.timeline.canvas.table.GetTimelineTableMeasurementValuesUseCase
import com.faltenreich.diaguard.timeline.canvas.table.GetTimelineTableStateUseCase
import com.faltenreich.diaguard.timeline.canvas.time.GetTimelineTimeStateUseCase
import com.faltenreich.diaguard.timeline.date.GetTimelineDateStateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlin.math.floor

class TimelineViewModel(
    getToday: GetTodayUseCase,
    getPropertyForChart: GetTimelineChartMeasurementPropertyUseCase,
    getValuesForChart: GetTimelineChartMeasurementValuesUseCase,
    getPropertiesForTable: GetTimelineTableMeasurementPropertiesUseCase,
    getValuesForTable: GetTimelineTableMeasurementValuesUseCase,
    getPreferenceUseCase: GetPreferenceUseCase,
    private val getDate: GetTimelineDateStateUseCase,
    private val getTime: GetTimelineTimeStateUseCase,
    private val getChart: GetTimelineChartStateUseCase,
    private val getTable: GetTimelineTableStateUseCase,
    private val pushScreen: PushScreenUseCase,
) : ViewModel<TimelineState, TimelineIntent, TimelineEvent>() {

    private val canvasSize = MutableStateFlow(Size.Unspecified)
    private val tableRowHeight = MutableStateFlow(0f)
    private val propertyForChart = getPropertyForChart()
    private val propertiesForTable = getPropertiesForTable()
    private val canvasDimensions = combine(canvasSize, tableRowHeight, propertiesForTable, TimelineCanvasDimensions::from)
    private val scrollOffset = MutableStateFlow(0f)

    private val initialDate = getToday()
    private val currentDate = MutableStateFlow(initialDate)
    private val date = combine(flowOf(initialDate), currentDate, getDate::invoke)
    private val time = combine(date, canvasDimensions, scrollOffset, getTime::invoke)

    private val decimalPlaces = getPreferenceUseCase(DecimalPlacesPreference)
    private val valuesForChart = currentDate.flatMapLatest(getValuesForChart::invoke)
    private val chart = combine(valuesForChart, propertyForChart, time, canvasDimensions, scrollOffset, getChart::invoke)
    private val valuesForTable = currentDate.flatMapLatest(getValuesForTable::invoke)
    private val table = combine(valuesForTable, propertiesForTable, time, decimalPlaces, canvasDimensions, scrollOffset, getTable::invoke)

    override val state = combine(date, time, chart, table, ::TimelineState)

    override suspend fun handleIntent(intent: TimelineIntent) {
        when (intent) {
            is TimelineIntent.Setup -> {
                canvasSize.update { intent.canvasSize }
                tableRowHeight.update { intent.tableRowHeight }
            }
            is TimelineIntent.Invalidate -> {
                val widthPerDay = canvasSize.value.width
                val threshold = (intent.scrollOffset * -1) + (widthPerDay / 2f)
                val offsetInDays = floor( threshold / widthPerDay)

                scrollOffset.update { intent.scrollOffset }
                currentDate.update { initialDate.plus(offsetInDays.toInt(), DateUnit.DAY) }
            }
            is TimelineIntent.SelectDate -> selectDate(intent.date)
            is TimelineIntent.SelectPreviousDate -> selectDate(currentDate.value.minus(1, DateUnit.DAY))
            is TimelineIntent.SelectNextDate -> selectDate(currentDate.value.plus(1, DateUnit.DAY))
            is TimelineIntent.CreateEntry -> pushScreen(EntryFormScreen())
            is TimelineIntent.SearchEntries -> pushScreen(EntrySearchScreen())
        }
    }

    private fun selectDate(date: Date) {
        val daysBetween = initialDate.daysBetween(date)
        val offset = canvasSize.value.width * -1 * daysBetween
        postEvent(TimelineEvent.Scroll(offset))
    }
}