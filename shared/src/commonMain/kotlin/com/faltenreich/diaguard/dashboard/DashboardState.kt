package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.dashboard.average.DashboardAverageState
import com.faltenreich.diaguard.dashboard.hba1c.DashboardHbA1cState
import com.faltenreich.diaguard.dashboard.latest.DashboardLatestState
import com.faltenreich.diaguard.dashboard.today.DashboardTodayState
import com.faltenreich.diaguard.statistic.trend.StatisticTrendState
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

data class DashboardState(
    val latest: DashboardLatestState?,
    val today: DashboardTodayState,
    val average: DashboardAverageState,
    val hbA1c: DashboardHbA1cState,
    val trend: StatisticTrendState,
) {

    class Preview : PreviewParameterProvider<DashboardState> {

        override val values = sequenceOf(
            DashboardState(
                latest = null,
                today = DashboardTodayState(
                    totalCount = 0,
                    hypoCount = 0,
                    hyperCount = 0,
                ),
                average = DashboardAverageState(
                    day = null,
                    week = null,
                    month = null,
                ),
                hbA1c = DashboardHbA1cState.Unknown,
                trend = StatisticTrendState(
                    intervals = emptyList(),
                    targetValue = 120.0,
                    maximumValue = 200.0,
                ),
            ),
        )
    }
}