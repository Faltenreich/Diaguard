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
import com.faltenreich.diaguard.shared.view.skeleton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entry_first_description
import diaguard.shared.generated.resources.placeholder

@Composable
fun LatestDashboardItem(
    state: DashboardState.LatestBloodSugar?,
    onClick: (Entry.Local?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val valueState = state as? DashboardState.LatestBloodSugar.Value
    Card(
        onClick = { onClick(valueState?.entry) },
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
                    text = valueState?.value?.value ?: getString(Res.string.placeholder),
                    modifier = Modifier.skeleton(show = state == null),
                    color = valueState?.tint?.getColor() ?: Color.Unspecified,
                    style = AppTheme.typography.displayLarge,
                )
                Text(
                    text = valueState?.timePassed ?: getString(Res.string.entry_first_description),
                    modifier = Modifier.skeleton(show = state == null),
                    style = AppTheme.typography.bodyMedium,
                )
            }
        }
    }
}