package com.faltenreich.diaguard.dashboard.hba1c

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.value.MeasurementValue

sealed interface DashboardHbA1cState {

    data class Latest(
        val entry: Entry.Local,
        val dateTime: String,
        val value: MeasurementValue.Localized,
    ) : DashboardHbA1cState

    data class Estimated(val value: MeasurementValue.Localized) : DashboardHbA1cState

    data object Unknown : DashboardHbA1cState
}