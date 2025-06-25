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
import com.faltenreich.diaguard.dashboard.hba1c.DashboardHbA1c
import com.faltenreich.diaguard.dashboard.latest.DashboardLatest
import com.faltenreich.diaguard.dashboard.today.DashboardToday
import com.faltenreich.diaguard.dashboard.trend.DashboardTrend
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter

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
            modifier = modifier.fillMaxWidth(),
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
                data = state?.average,
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
private fun Preview(
    @PreviewParameter(DashboardState.Preview::class)
    state: DashboardState,
) {
    Dashboard(
        state = state,
        onIntent = {},
    )
}