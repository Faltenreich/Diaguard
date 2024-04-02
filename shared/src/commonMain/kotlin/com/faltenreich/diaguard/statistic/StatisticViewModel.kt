package com.faltenreich.diaguard.statistic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.list.GetMeasurementPropertiesUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class StatisticViewModel(
    getToday: GetTodayUseCase = inject(),
    getMeasurementProperties: GetMeasurementPropertiesUseCase,
    private val getAverage: GetAverageUseCase,
    private val dateTimeFormatter: DateTimeFormatter,
) : ViewModel<StatisticViewState, StatisticIntent>() {

    private val selectedProperty = MutableStateFlow<MeasurementProperty?>(null)
    private val initialDateRange = getToday().let { today ->
        today.minus(1, DateUnit.WEEK) .. today
    }
    var dateRange by mutableStateOf(initialDateRange)
    val dateRangeLocalized: String
        get() = dateTimeFormatter.formatDateRange(dateRange)

    override val state = combine(
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

    override fun handleIntent(intent: StatisticIntent) {
        when (intent) {
            is StatisticIntent.Select -> selectedProperty.value = intent.property
        }
    }
}