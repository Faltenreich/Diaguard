package com.faltenreich.diaguard.dashboard.trend

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.dashboard.DashboardState
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.placeholder
import org.jetbrains.compose.resources.stringResource

@Composable
fun TrendChart(
    state: DashboardState.Trend?,
    modifier: Modifier = Modifier,
) {
    // TODO
    Text(state?.values?.size?.toString() ?: stringResource(Res.string.placeholder))
}