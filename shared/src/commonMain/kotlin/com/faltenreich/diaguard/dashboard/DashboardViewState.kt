package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.value.tint.MeasurementValueTint

sealed interface DashboardViewState {

    data class Revisit(
        val latestBloodSugar: LatestBloodSugar? = null,
        val today: Today? = null,
        val average: Average? = null,
        val hbA1c: HbA1c? = null,
        val trend: Trend? = null,
    ) : DashboardViewState {

        data class LatestBloodSugar(
            val entry: Entry,
            val value: String,
            val tint: MeasurementValueTint,
            val timePassed: String,
        )

        data class Today(
            val totalCount: Int,
            val hypoCount: Int,
            val hyperCount: Int,
        )

        data class Average(
            val day: String?,
            val week: String?,
            val month: String?,
        )

        data class HbA1c(
            val value: String,
        )

        data class Trend(
            val values: Map<Date, Int>,
        )
    }
}