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
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.statistic.trend.StatisticTrendChart
import com.faltenreich.diaguard.statistic.trend.StatisticTrendState
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.trend

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
                    modifier = Modifier.height(AppTheme.dimensions.size.TrendHeight),
                )
            } else {
                Spacer(modifier = Modifier.height(AppTheme.dimensions.size.TrendHeight))
            }
        }
    }
}