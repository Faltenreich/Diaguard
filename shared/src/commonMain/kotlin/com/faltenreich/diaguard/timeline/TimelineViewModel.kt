package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.deep
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TimelineViewModel(
    date: Date?,
    private val dispatcher: CoroutineDispatcher = inject(),
    dateTimeFactory: DateTimeFactory = inject(),
    entryRepository: EntryRepository = inject(),
    measurementPropertyRepository: MeasurementPropertyRepository = inject(),
) : ViewModel() {

    private val initialDate = date ?: dateTimeFactory.today()
    private val currentDate = MutableStateFlow(initialDate)
    private val entries: Flow<List<Entry>> = currentDate.flatMapLatest { date ->
        entryRepository.observeByDateRange(
            startDateTime = date.minusDays(1).atTime(Time.atStartOfDay()),
            endDateTime = date.plusDays(1).atTime(Time.atEndOfDay()),
        ).deep()
    }
    private val values: Flow<Pair<List<MeasurementValue>, List<MeasurementValue>>> = entries.map { entries ->
        entries
            .flatMap(Entry::values)
            .partition { value -> value.type.property.isBloodSugar }

    }
    private val valuesForChart = values.map { it.first }
    private val valuesForList = values.map { it.second }
    private val propertiesForList = measurementPropertyRepository.observeAll().map { properties ->
        properties.filterNot(MeasurementProperty::isBloodSugar)
    }

    private val state = combine(
        flowOf(initialDate),
        currentDate,
        valuesForChart,
        valuesForList,
        propertiesForList,
        ::TimelineViewState,
    ).flowOn(dispatcher)

    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = TimelineViewState(
            initialDate = initialDate,
            currentDate = initialDate,
            valuesForChart = emptyList(),
            valuesForList = emptyList(),
            propertiesForList = emptyList(),
        ),
    )

    fun setDate(date: Date) = viewModelScope.launch(dispatcher) {
        currentDate.value = date
    }
}