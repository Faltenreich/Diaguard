package com.faltenreich.diaguard.statistic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.list.GetMeasurementPropertiesUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.datetime.DateTimeFormatter
import com.faltenreich.diaguard.shared.datetime.DateUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class StatisticViewModel(
    getMeasurementProperties: GetMeasurementPropertiesUseCase,
    private val getAverage: GetAverageUseCase,
    dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
) : ViewModel() {

    private val selectedProperty = MutableStateFlow<MeasurementProperty?>(null)
    private val initialDateRange = dateTimeFactory.today().let { today ->
        today.minus(1, DateUnit.WEEK) .. today
    }
    var dateRange by mutableStateOf(initialDateRange)
    val dateRangeLocalized: String
        get() = dateTimeFormatter.formatDateRange(dateRange)

    private val state = combine(
        getMeasurementProperties(),
        selectedProperty,
    ) { properties, selectedProperty ->
        val property = selectedProperty ?: properties.first()
        StatisticViewState.Loaded(
            properties = properties,
            selectedProperty = property,
            dateRange = dateRange,
            dateRangeLocalized = dateRangeLocalized,
            average = getAverage(property, dateRange),
        )
    }
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = StatisticViewState.Loading,
    )

    fun selectProperty(property: MeasurementProperty) {
        selectedProperty.value = property
    }
}