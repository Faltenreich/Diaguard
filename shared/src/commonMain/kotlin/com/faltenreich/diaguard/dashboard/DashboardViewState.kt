package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.value.MeasurementValueForUser
import com.faltenreich.diaguard.measurement.value.tint.MeasurementValueTint

data class DashboardViewState(
    val latestBloodSugar: LatestBloodSugar?,
    val today: Today?,
    val average: Average?,
    val hbA1c: HbA1c?,
    val trend: Trend?,
) {

    data class LatestBloodSugar(
        val entry: Entry.Local,
        val value: MeasurementValueForUser,
        val tint: MeasurementValueTint,
        val timePassed: String,
    )

    data class Today(
        val totalCount: Int,
        val hypoCount: Int,
        val hyperCount: Int,
    )

    data class Average(
        val day: MeasurementValueForUser?,
        val week: MeasurementValueForUser?,
        val month: MeasurementValueForUser?,
    )

    data class HbA1c(
        val value: MeasurementValueForUser,
    )

    data class Trend(
        val values: Map<Date, Int>,
    )
}