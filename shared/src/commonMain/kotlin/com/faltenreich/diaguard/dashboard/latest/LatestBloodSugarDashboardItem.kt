package com.faltenreich.diaguard.dashboard.latest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.dashboard.DashboardState
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entry_first_description
import diaguard.shared.generated.resources.placeholder

@Composable
fun LatestDashboardItem(
    data: DashboardState.LatestBloodSugar?,
    onClick: (Entry.Local?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = { onClick(data?.entry) },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.dimensions.padding.P_3),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = data?.value?.value ?: getString(Res.string.placeholder),
                    style = AppTheme.typography.displayLarge,
                    color = data?.tint?.getColor() ?: Color.Unspecified,
                )
                Text(
                    text = data?.timePassed ?: getString(Res.string.entry_first_description),
                    style = AppTheme.typography.bodyMedium,
                )
            }
        }
    }
}