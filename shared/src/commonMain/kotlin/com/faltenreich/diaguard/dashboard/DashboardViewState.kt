package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.shared.datetime.Date

sealed interface DashboardViewState {

    data object Unknown : DashboardViewState

    data object FirstVisit : DashboardViewState

    data class Revisit(
        val bloodSugar: BloodSugar,
        val today: Today,
        val average: Average,
        val hbA1c: HbA1c,
        val trend: Trend,
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