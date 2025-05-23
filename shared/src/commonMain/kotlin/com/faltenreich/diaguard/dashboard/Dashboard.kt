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
import com.faltenreich.diaguard.dashboard.average.AverageDashboardItem
import com.faltenreich.diaguard.dashboard.hba1c.HbA1cDashboardItem
import com.faltenreich.diaguard.dashboard.latest.LatestDashboardItem
import com.faltenreich.diaguard.dashboard.today.TodayDashboardItem
import com.faltenreich.diaguard.dashboard.trend.TrendDashboardItem

@Composable
fun Dashboard(
    viewModel: DashboardViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState()
    Column(
        modifier = modifier
            .padding(all = AppTheme.dimensions.padding.P_3)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
    ) {
        LatestDashboardItem(
            state = state?.latestBloodSugar,
            onClick = { entry ->
                val intent = entry?.let(DashboardIntent::EditEntry) ?: DashboardIntent.CreateEntry
                viewModel.dispatchIntent(intent)
            },
            modifier = modifier.fillMaxWidth(),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
        ) {
            TodayDashboardItem(
                state = state?.today,
                onClick = { viewModel.dispatchIntent(DashboardIntent.OpenStatistic) },
                modifier = Modifier.weight(1f),
            )
            AverageDashboardItem(
                data = state?.average,
                onClick = { viewModel.dispatchIntent(DashboardIntent.OpenStatistic) },
                modifier = Modifier.weight(1f),
            )
        }
        HbA1cDashboardItem(
            state = state?.hbA1c,
            onOpenEntry = { entry -> viewModel.dispatchIntent(DashboardIntent.EditEntry(entry = entry)) },
            modifier = Modifier.fillMaxWidth(),
        )
        TrendDashboardItem(
            state = state?.trend,
            onClick = { viewModel.dispatchIntent(DashboardIntent.OpenStatistic) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}