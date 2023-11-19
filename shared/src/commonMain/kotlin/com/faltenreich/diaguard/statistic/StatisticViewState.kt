package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.shared.datetime.Date

sealed interface StatisticViewState {

    data object Loading : StatisticViewState

    data class Loaded(
        val properties: List<MeasurementProperty>,
        val selectedProperty: MeasurementProperty,
        val dateRange: ClosedRange<Date>,
        val dateRangeLocalized: String,
    ) : StatisticViewState
}