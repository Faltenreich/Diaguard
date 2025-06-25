package com.faltenreich.diaguard.dashboard.latest

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.tint.MeasurementValueTint
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

sealed interface DashboardLatestState {

    data object None : DashboardLatestState

    data class Value(
        val entry: Entry.Local,
        val value: MeasurementValue.Localized,
        val tint: MeasurementValueTint,
        val timePassed: String,
    ) : DashboardLatestState

    class Preview : PreviewParameterProvider<DashboardLatestState> {

        override val values = sequenceOf(
            None,
        )
    }
}