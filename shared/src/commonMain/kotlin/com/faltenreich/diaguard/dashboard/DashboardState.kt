package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.tint.MeasurementValueTint

data class DashboardState(
    val latestBloodSugar: LatestBloodSugar?,
    val today: Today?,
    val average: Average?,
    val hbA1c: HbA1c?,
    val trend: Trend?,
) {

    data class LatestBloodSugar(
        val entry: Entry.Local,
        val value: MeasurementValue.Localized,
        val tint: MeasurementValueTint,
        val timePassed: String,
    )

    data class Today(
        val totalCount: Int,
        val hypoCount: Int,
        val hyperCount: Int,
    )

    data class Average(
        val day: MeasurementValue.Localized?,
        val week: MeasurementValue.Localized?,
        val month: MeasurementValue.Localized?,
    )

    data class HbA1c(
        val label: String,
        val value: MeasurementValue.Localized?,
    )

    data class Trend(
        val values: Map<Date, Int>,
    )
}