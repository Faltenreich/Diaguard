package com.faltenreich.diaguard.dashboard.card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.dashboard.DashboardViewState

@Composable
fun TrendDashboardItem(
    data: DashboardViewState.Revisit.Trend?,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Box(modifier = Modifier.padding(all = AppTheme.dimensions.padding.P_2)) {
            Text("Trend")
        }
    }
}