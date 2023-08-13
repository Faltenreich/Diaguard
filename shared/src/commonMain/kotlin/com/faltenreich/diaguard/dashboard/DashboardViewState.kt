package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.shared.datetime.Date

sealed interface DashboardViewState {

    data object Loading: DashboardViewState

    data object FirstVisit : DashboardViewState

    data class Revisit(
        val bloodSugar: BloodSugar? = null,
        val today: Today? = null,
        val average: Average? = null,
        val hbA1c: HbA1c? = null,
        val trend: Trend? = null,
    ) : DashboardViewState {

        data class BloodSugar(
            val value: String,
            val dateTime: String,
            val ago: String,
        )

        data class Today(
            val totalCount: String,
            val hyperCount: String,
            val hypoCount: String,
        )

        data class Average(
            val day: String,
            val week: String,
            val month: String,
        )

        data class HbA1c(
            val value: String,
        )

        data class Trend(
            val values: Map<Date, Int>,
        )
    }
}