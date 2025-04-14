package com.faltenreich.diaguard.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.dashboard.average.AverageDashboardItem
import com.faltenreich.diaguard.dashboard.hba1c.HbA1cDashboardItem
import com.faltenreich.diaguard.dashboard.latest.LatestDashboardItem
import com.faltenreich.diaguard.dashboard.today.TodayDashboardItem
import com.faltenreich.diaguard.dashboard.trend.TrendDashboardItem
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBar
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bar.top.TopAppBar
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.app_name
import diaguard.shared.generated.resources.entry_new_description
import diaguard.shared.generated.resources.ic_add
import diaguard.shared.generated.resources.ic_search
import diaguard.shared.generated.resources.search_open
import org.jetbrains.compose.resources.painterResource

@Composable
fun Dashboard(
    viewModel: DashboardViewModel,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(getString(Res.string.app_name)) }) },
        bottomBar = {
            BottomAppBar(
                onMenuClick = { viewModel.dispatchIntent(DashboardIntent.OpenMainMenu) },
                actions = {
                    BottomAppBarItem(
                        painter = painterResource(Res.drawable.ic_search),
                        contentDescription = Res.string.search_open,
                        onClick = { viewModel.dispatchIntent(DashboardIntent.SearchEntries) },
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { viewModel.dispatchIntent(DashboardIntent.CreateEntry) },
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_add),
                            contentDescription = getString(Res.string.entry_new_description),
                        )
                    }
                }
            )
        }
    ) { padding ->
        when (val state = viewModel.collectState()) {
            null -> Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
            else -> Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(AppTheme.dimensions.padding.P_3)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
            ) {
                LatestDashboardItem(
                    data = state.latestBloodSugar,
                    onClick = { entry ->
                        val intent = entry?.let(DashboardIntent::EditEntry) ?: DashboardIntent.CreateEntry
                        viewModel.dispatchIntent(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
                ) {
                    TodayDashboardItem(
                        data = state.today,
                        onClick = { viewModel.dispatchIntent(DashboardIntent.OpenStatistic) },
                        modifier = Modifier.weight(1f),
                    )
                    AverageDashboardItem(
                        data = state.average,
                        onClick = { viewModel.dispatchIntent(DashboardIntent.OpenStatistic) },
                        modifier = Modifier.weight(1f),
                    )
                }
                HbA1cDashboardItem(
                    data = state.hbA1c,
                    modifier = Modifier.fillMaxWidth(),
                )
                TrendDashboardItem(
                    data = state.trend,
                    onClick = { viewModel.dispatchIntent(DashboardIntent.OpenStatistic) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}