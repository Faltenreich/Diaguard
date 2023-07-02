package com.faltenreich.diaguard.timeline

import androidx.compose.ui.geometry.Offset
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.deep
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TimelineViewModel(
    initialDate: Date,
    dispatcher: CoroutineDispatcher = inject(),
    entryRepository: EntryRepository = inject(),
    measurementPropertyRepository: MeasurementPropertyRepository = inject(),
) : ViewModel() {

    private val currentDate = MutableStateFlow(initialDate)
    private val entries = currentDate.flatMapLatest { date ->
        entryRepository.observeByDateRange(
            startDateTime = date.minusDays(1).atTime(Time.atStartOfDay()),
            endDateTime = date.plusDays(1).atTime(Time.atEndOfDay()),
        ).deep()
    }
    private val valuesForChart = entries.map { entries ->
        entries
            .map(Entry::values)
            .flatMap { values ->
                // TODO: Identify Blood Sugar
                values.filter { value -> value.type.property.id == 1L }
            }
    }
    private val propertiesForList = measurementPropertyRepository.observeAll().map { properties ->
        // TODO: Identify Blood Sugar
        properties.filterNot { property -> property.id == 1L }
    }


    private val state = combine(
        flowOf(Offset.Zero),
        flowOf(initialDate),
        currentDate,
        valuesForChart,
        propertiesForList,
        ::TimelineViewState,
    ).flowOn(dispatcher)

    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = TimelineViewState(
            offset = Offset.Zero,
            initialDate = initialDate,
            currentDate = initialDate,
            valuesForChart = emptyList(),
            propertiesForList = emptyList(),
        ),
    )

    fun setDate(date: Date) {
        currentDate.value = date
    }
}