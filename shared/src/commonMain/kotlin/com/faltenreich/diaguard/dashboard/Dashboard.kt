package com.faltenreich.diaguard.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.dashboard.card.AverageDashboardItem
import com.faltenreich.diaguard.dashboard.card.HbA1cDashboardItem
import com.faltenreich.diaguard.dashboard.card.LatestDashboardItem
import com.faltenreich.diaguard.dashboard.card.TodayDashboardItem
import com.faltenreich.diaguard.dashboard.card.TrendDashboardItem
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.LoadingIndicator

@Composable
fun Dashboard(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = inject(),
) {
    when (val state = viewModel.collectState()) {
        null -> LoadingIndicator()
        is DashboardViewState.Revisit -> Column(
            modifier = modifier.padding(
                horizontal = AppTheme.dimensions.padding.P_3,
                vertical = AppTheme.dimensions.padding.P_2,
            ),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
        ) {
            LatestDashboardItem(
                data = state.latestBloodSugar,
                modifier = modifier.fillMaxWidth(),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
            ) {
                TodayDashboardItem(
                    data = state.today,
                    modifier = Modifier.weight(1f),
                )
                AverageDashboardItem(
                    data = state.average,
                    modifier = Modifier.weight(1f),
                )
            }
            HbA1cDashboardItem(
                data = state.hbA1c,
                modifier = Modifier.fillMaxWidth(),
            )
            TrendDashboardItem(
                data = state.trend,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}