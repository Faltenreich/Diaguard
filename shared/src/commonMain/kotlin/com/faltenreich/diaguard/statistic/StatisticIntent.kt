package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.measurement.property.MeasurementProperty

sealed interface StatisticIntent {

    data class Select(val property: MeasurementProperty) : StatisticIntent
}