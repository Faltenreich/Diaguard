package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.tint.MeasurementValueTint

data class DashboardState(
    val latestBloodSugar: LatestBloodSugar?,
    val today: Today,
    val average: Average,
    val hbA1c: HbA1c,
    val trend: Trend,
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

    sealed interface HbA1c {

        data class Latest(
            val entry: Entry.Local,
            val dateTime: String,
            val value: MeasurementValue.Localized,
        ) : HbA1c

        data class Estimated(val value: MeasurementValue.Localized) : HbA1c

        data object Unknown : HbA1c
    }

    data class Trend(
        val days: List<Day>,
        val targetValue: Double,
        val maximumValue: Double,
    ) {

        data class Day(
            val date: String,
            val average: Double?,
        )
    }
}