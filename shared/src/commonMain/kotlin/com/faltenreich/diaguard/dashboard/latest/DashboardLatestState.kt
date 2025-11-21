package com.faltenreich.diaguard.dashboard.latest

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueTint

sealed interface DashboardLatestState {

    data object None : DashboardLatestState

    data class Value(
        val entry: Entry.Local,
        val value: MeasurementValue.Localized,
        val tint: MeasurementValueTint,
        val timePassed: String,
    ) : DashboardLatestState
}