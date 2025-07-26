package com.faltenreich.diaguard.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.dashboard.average.DashboardAverage
import com.faltenreich.diaguard.dashboard.average.DashboardAverageState
import com.faltenreich.diaguard.dashboard.hba1c.DashboardHbA1c
import com.faltenreich.diaguard.dashboard.hba1c.DashboardHbA1cState
import com.faltenreich.diaguard.dashboard.latest.DashboardLatest
import com.faltenreich.diaguard.dashboard.latest.DashboardLatestState
import com.faltenreich.diaguard.dashboard.today.DashboardToday
import com.faltenreich.diaguard.dashboard.today.DashboardTodayState
import com.faltenreich.diaguard.dashboard.trend.DashboardTrend
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import com.faltenreich.diaguard.statistic.trend.StatisticTrendState
import com.faltenreich.diaguard.statistic.trend.StatisticTrendState.Interval
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Dashboard(
    state: DashboardState?,
    onIntent: (DashboardIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(all = AppTheme.dimensions.padding.P_3)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
    ) {
        DashboardLatest(
            state = state?.latest,
            onClick = { entry ->
                onIntent(entry?.let(DashboardIntent::EditEntry) ?: DashboardIntent.CreateEntry)
            },
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
        ) {
            DashboardToday(
                state = state?.today,
                onClick = { onIntent(DashboardIntent.OpenStatistic) },
                modifier = Modifier.weight(1f),
            )
            DashboardAverage(
                state = state?.average,
                onClick = { onIntent(DashboardIntent.OpenStatistic) },
                modifier = Modifier.weight(1f),
            )
        }
        DashboardHbA1c(
            state = state?.hbA1c,
            onOpenEntry = { entry -> onIntent(DashboardIntent.EditEntry(entry = entry)) },
            modifier = Modifier.fillMaxWidth(),
        )
        DashboardTrend(
            state = state?.trend,
            onClick = { onIntent(DashboardIntent.OpenStatistic) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    Dashboard(
        state = DashboardState(
            latest = DashboardLatestState.None,
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
                intervals = week().map { date ->
                    Interval(
                        dateRange = date .. date,
                        label = date.dayOfWeek.localized(),
                        average = null,
                    )
                },
                targetValue = 120.0,
                maximumValue = 200.0,
            ),
        ),
        onIntent = {},
    )
}