package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.Date

sealed interface DashboardViewState {

    data object Loading: DashboardViewState

    data object FirstVisit : DashboardViewState

    data class Revisit(
        val latestBloodSugar: LatestBloodSugar? = null,
        val today: Today? = null,
        val average: Average? = null,
        val hbA1c: HbA1c? = null,
        val trend: Trend? = null,
    ) : DashboardViewState {

        data class LatestBloodSugar(
            val value: MeasurementValue,
        )

        data class Today(
            val totalCount: Int,
            val hyperCount: Int,
            val hypoCount: Int,
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