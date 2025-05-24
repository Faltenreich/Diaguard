package com.faltenreich.diaguard.dashboard.average

import com.faltenreich.diaguard.measurement.value.MeasurementValue

data class DashboardAverageState(
    val day: MeasurementValue.Localized?,
    val week: MeasurementValue.Localized?,
    val month: MeasurementValue.Localized?,
)