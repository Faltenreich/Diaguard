package com.faltenreich.diaguard.dashboard.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.dashboard.DashboardViewState

@Composable
fun LatestDashboardItem(
    data: DashboardViewState.BloodSugar?,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.dimensions.padding.P_2),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("400")
            Row {
                Text("Date and time")
                Text("x ago")
            }
        }
    }
}