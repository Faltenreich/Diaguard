package com.faltenreich.diaguard.dashboard.trend

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.datetime.DateProgression
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import com.faltenreich.diaguard.statistic.trend.StatisticTrendChart
import com.faltenreich.diaguard.statistic.trend.StatisticTrendState
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.trend
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DashboardTrend(
    state: StatisticTrendState?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
    ) {
        Column(modifier = Modifier.padding(all = AppTheme.dimensions.padding.P_3)) {
            Text(
                text = getString(Res.string.trend),
                style = AppTheme.typography.labelMedium,
            )
            if (state != null) {
                StatisticTrendChart(
                    state = state,
                    modifier = Modifier.height(AppTheme.dimensions.size.DashboardTrendHeight),
                )
            } else {
                Spacer(modifier = Modifier.height(AppTheme.dimensions.size.DashboardTrendHeight))
            }
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    DashboardTrend(
        state = StatisticTrendState(
            intervals = today().let { today ->
                DateProgression(
                    start = today
                        .minus(1, DateUnit.WEEK)
                        .plus(1, DateUnit.DAY),
                    endInclusive = today,
                ).map { date ->
                    StatisticTrendState.Interval(
                        dateRange = date .. date,
                        label = date.dayOfWeek.localized(),
                        average = null,
                    )
                }
            },
            targetValue = 120.0,
            maximumValue = 200.0,
        ),
        onClick = {},
    )
}