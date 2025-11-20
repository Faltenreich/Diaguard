package com.faltenreich.diaguard.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.view.theme.AppTheme
import com.faltenreich.diaguard.dashboard.average.DashboardAverage
import com.faltenreich.diaguard.dashboard.average.DashboardAverageState
import com.faltenreich.diaguard.dashboard.hba1c.DashboardHbA1c
import com.faltenreich.diaguard.dashboard.hba1c.DashboardHbA1cState
import com.faltenreich.diaguard.dashboard.latest.DashboardLatest
import com.faltenreich.diaguard.dashboard.latest.DashboardLatestState
import com.faltenreich.diaguard.dashboard.reminder.DashboardReminder
import com.faltenreich.diaguard.dashboard.reminder.DashboardReminderState
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
    ) {
        DashboardLatest(
            state = state?.latest,
            onClick = { entry ->
                onIntent(entry?.let(DashboardIntent::EditEntry) ?: DashboardIntent.CreateEntry)
            },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))

        AnimatedVisibility(visible = state?.reminder != null) {
            val reminder = remember { state?.reminder }
            Column {
                DashboardReminder(
                    state = reminder,
                    onDelete = { onIntent(DashboardIntent.DeleteReminder) },
                    modifier = Modifier.fillMaxWidth(),
                )
                // Spacings are applied manually to avoid jumping layout when animating reminder
                Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))
            }

        }

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
        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))

        DashboardHbA1c(
            state = state?.hbA1c,
            onOpenEntry = { entry -> onIntent(DashboardIntent.EditEntry(entry = entry)) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))

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
            reminder = DashboardReminderState(
                text = "Reminder in 5 minutes",
            ),
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