package com.faltenreich.diaguard.timeline

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.list.EntryListItemState
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormScreen
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.timeline.canvas.GetTimelineCanvasDimensionsUseCase
import com.faltenreich.diaguard.timeline.canvas.TapTimelineCanvasResult
import com.faltenreich.diaguard.timeline.canvas.TapTimelineCanvasUseCase
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvasDimensions
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvasState
import com.faltenreich.diaguard.timeline.canvas.chart.GetTimelineChartMeasurementPropertyUseCase
import com.faltenreich.diaguard.timeline.canvas.chart.GetTimelineChartMeasurementValuesUseCase
import com.faltenreich.diaguard.timeline.canvas.chart.GetTimelineChartStateUseCase
import com.faltenreich.diaguard.timeline.canvas.table.GetTimelineTableMeasurementPropertiesUseCase
import com.faltenreich.diaguard.timeline.canvas.table.GetTimelineTableMeasurementValuesUseCase
import com.faltenreich.diaguard.timeline.canvas.table.GetTimelineTableStateUseCase
import com.faltenreich.diaguard.timeline.canvas.time.GetTimelineTimeStateUseCase
import com.faltenreich.diaguard.timeline.date.GetTimelineDateStateUseCase
import com.faltenreich.diaguard.timeline.date.TimelineDateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlin.math.floor

class TimelineViewModel(
    getToday: GetTodayUseCase,
    getCanvasDimensions: GetTimelineCanvasDimensionsUseCase,
    getPropertyForChart: GetTimelineChartMeasurementPropertyUseCase,
    getValuesForChart: GetTimelineChartMeasurementValuesUseCase,
    getPropertiesForTable: GetTimelineTableMeasurementPropertiesUseCase,
    getValuesForTable: GetTimelineTableMeasurementValuesUseCase,
    getPreferenceUseCase: GetPreferenceUseCase,
    private val getDate: GetTimelineDateStateUseCase,
    private val getTime: GetTimelineTimeStateUseCase,
    private val getChart: GetTimelineChartStateUseCase,
    private val getTable: GetTimelineTableStateUseCase,
    private val tapCanvas: TapTimelineCanvasUseCase,
    private val pushScreen: PushScreenUseCase,
) : ViewModel<TimelineState, TimelineIntent, TimelineEvent>() {

    private val propertiesForTable = getPropertiesForTable()
    private val decimalPlaces = getPreferenceUseCase(DecimalPlacesPreference)

    private val positions = MutableStateFlow<TimelineCanvasDimensions.Positioned?>(null)
    private val scrollOffset = MutableStateFlow(0f)
    private val dimensions = combine(
        positions,
        scrollOffset,
        propertiesForTable,
        getCanvasDimensions::invoke,
    )

    private val initialDate = getToday()
    private val currentDate = MutableStateFlow(initialDate)
    private val datePickerDialog = MutableStateFlow<TimelineDateState.PickerDialog?>(null)
    private val date = combine(flowOf(initialDate), currentDate, datePickerDialog, getDate::invoke)
    private val time = combine(date, dimensions, getTime::invoke)

    private val chart = combine(
        currentDate.flatMapLatest(getValuesForChart::invoke),
        getPropertyForChart(),
        decimalPlaces,
        time,
        dimensions,
        getChart::invoke,
    )

    private val table = combine(
        currentDate.flatMapLatest(getValuesForTable::invoke),
        propertiesForTable,
        decimalPlaces,
        time,
        dimensions,
        getTable::invoke,
    )

    private val canvas = combine(time, chart, table) { time, chart, table ->
        if (time != null && chart != null && table != null) TimelineCanvasState(time, chart, table)
        else null
    }

    private val valueBottomSheet = MutableStateFlow<TimelineState.EntryListBottomSheet?>(null)

    override val state = combine(date, canvas, valueBottomSheet, ::TimelineState)

    override suspend fun handleIntent(intent: TimelineIntent) {
        when (intent) {
            is TimelineIntent.Setup -> {
                positions.update {
                    TimelineCanvasDimensions.Positioned(
                        canvasSize = intent.canvasSize,
                        tableRowHeight = intent.tableRowHeight,
                        statusBarHeight = intent.statusBarHeight,
                    )
                }
            }
            is TimelineIntent.Invalidate -> {
                val canvasSize = positions.value?.canvasSize ?: return
                val widthPerDay = canvasSize.width
                val threshold = (intent.scrollOffset * -1) + (widthPerDay / 2f)
                val offsetInDays = floor( threshold / widthPerDay)

                scrollOffset.update { intent.scrollOffset }
                currentDate.update { initialDate.plus(offsetInDays.toInt(), DateUnit.DAY) }
            }
            is TimelineIntent.TapCanvas -> tapCanvas(intent.position, intent.touchAreaSize)
            is TimelineIntent.SelectDate -> selectDate(intent.date)
            is TimelineIntent.SelectPreviousDate -> selectDate(currentDate.value.minus(1, DateUnit.DAY))
            is TimelineIntent.SelectNextDate -> selectDate(currentDate.value.plus(1, DateUnit.DAY))
            is TimelineIntent.SelectToday -> selectDate(initialDate)
            is TimelineIntent.OpenDatePicker ->
                datePickerDialog.update { TimelineDateState.PickerDialog(currentDate.value) }
            is TimelineIntent.DismissDatePicker -> datePickerDialog.update { null }
            is TimelineIntent.CreateEntry -> pushScreen(EntryFormScreen())
            is TimelineIntent.OpenEntry -> pushScreen(EntryFormScreen(intent.entry))
            is TimelineIntent.OpenEntryListBottomSheet ->
                valueBottomSheet.update { TimelineState.EntryListBottomSheet(intent.entries) }
            is TimelineIntent.DismissEntryListBottomSheet -> valueBottomSheet.update { null }
            is TimelineIntent.OpenEntrySearch -> pushScreen(EntrySearchScreen())
        }
    }

    private suspend fun tapCanvas(
        position: Offset,
        touchAreaSize: Size,
    ) {
        val canvas = canvas.firstOrNull() ?: return
        when (val result = tapCanvas(position, touchAreaSize, canvas)) {
            is TapTimelineCanvasResult.Icon -> pushScreen(MeasurementPropertyFormScreen(result.property))
            is TapTimelineCanvasResult.Chart -> openEntryOrList(result.entries)
            is TapTimelineCanvasResult.Table -> openEntryOrList(result.entries)
            is TapTimelineCanvasResult.None -> Unit
        }
    }

    private suspend fun openEntryOrList(entries: List<EntryListItemState>) {
        if (entries.size == 1) {
            pushScreen(EntryFormScreen(entries.first().entry))
        } else {
            dispatchIntent(TimelineIntent.OpenEntryListBottomSheet(entries))
        }
    }

    private fun selectDate(date: Date) {
        val canvasSize = positions.value?.canvasSize ?: return
        val daysBetween = initialDate.daysBetween(date)
        val offset = canvasSize.width * -1 * daysBetween
        postEvent(TimelineEvent.Scroll(offset))
    }
}