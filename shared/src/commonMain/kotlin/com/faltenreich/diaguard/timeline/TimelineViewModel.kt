package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.deep
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.navigation.NavigateToUseCase
import com.faltenreich.diaguard.navigation.screen.EntryFormScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.datetime.DateUnit
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class TimelineViewModel(
    date: Date?,
    dateTimeFactory: DateTimeFactory = inject(),
    entryRepository: EntryRepository = inject(),
    measurementPropertyRepository: MeasurementPropertyRepository = inject(),
    private val navigateTo: NavigateToUseCase = inject(),
) : ViewModel<TimelineViewState, TimelineIntent>() {

    private val initialDate = date ?: dateTimeFactory.today()
    private val currentDate = MutableStateFlow(initialDate)
    private val entries: Flow<List<Entry>> = currentDate.flatMapLatest { date ->
        entryRepository.observeByDateRange(
            startDateTime = date.minus(1, DateUnit.DAY).atStartOfDay(),
            endDateTime = date.plus(1, DateUnit.DAY).atEndOfDay(),
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

    override val state = combine(
        flowOf(initialDate),
        currentDate,
        valuesForChart,
        valuesForList,
        propertiesForList,
        ::TimelineViewState,
    )

    override fun onIntent(intent: TimelineIntent) {
        when (intent) {
            is TimelineIntent.CreateEntry -> navigateTo(EntryFormScreen())
            is TimelineIntent.SetDate -> currentDate.value = intent.date
        }
    }
}