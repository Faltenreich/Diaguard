package com.faltenreich.diaguard.dashboard.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.shared.localization.getString

@Composable
fun AverageDashboardItem(
    data: DashboardViewState.Revisit.Average?,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Box(modifier = Modifier.padding(all = AppTheme.dimensions.padding.P_3)) {
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3)) {
                Text(
                    text = getString(Res.string.average),
                    style = AppTheme.typography.labelMedium,
                )
                Row {
                    Text(
                        text = getString(Res.string.day),
                        modifier = Modifier.weight(1f),
                    )
                    Text(data?.day ?: getString(Res.string.placeholder))
                }
                Row {
                    Text(
                        text = getString(Res.string.week),
                        modifier = Modifier.weight(1f),
                    )
                    Text(data?.week ?: getString(Res.string.placeholder))
                }
                Row {
                    Text(
                        text = getString(Res.string.month),
                        modifier = Modifier.weight(1f),
                    )
                    Text(data?.month ?: getString(Res.string.placeholder))
                }
            }
        }
    }
}