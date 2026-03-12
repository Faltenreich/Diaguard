package com.faltenreich.diaguard.statistic.property

import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty

data class StatisticPropertyState(
    val properties: List<MeasurementProperty.Local>,
    val selection: MeasurementProperty.Local,
)