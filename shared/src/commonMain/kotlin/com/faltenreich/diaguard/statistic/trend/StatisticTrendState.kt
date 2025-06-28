package com.faltenreich.diaguard.statistic.trend

import com.faltenreich.diaguard.datetime.DateProgression
import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.measurement.value.tint.MeasurementValueTint
import com.faltenreich.diaguard.shared.view.preview.PreviewScope

data class StatisticTrendState(
    val intervals: List<Interval>,
    val targetValue: Double,
    val maximumValue: Double,
) {

    data class Interval(
        val dateRange: DateRange,
        val label: String,
        val average: Value?,
    )

    data class Value(
        val value: Double,
        val tint: MeasurementValueTint,
    )

    companion object {

        fun preview(scope: PreviewScope) = with(scope) {
            StatisticTrendState(
                intervals = today().let { today ->
                    DateProgression(
                        start = today
                            .minus(1, DateUnit.WEEK)
                            .plus(1, DateUnit.DAY),
                        endInclusive = today,
                    ).map { date ->
                        Interval(
                            dateRange = date .. date,
                            label = date.dayOfWeek.localized(),
                            average = null,
                        )
                    }
                },
                targetValue = 120.0,
                maximumValue = 200.0,
            )
        }
    }
}