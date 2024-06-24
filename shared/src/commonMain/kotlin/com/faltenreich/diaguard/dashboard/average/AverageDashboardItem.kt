package com.faltenreich.diaguard.dashboard.average

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
import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.average
import diaguard.shared.generated.resources.day
import diaguard.shared.generated.resources.month
import diaguard.shared.generated.resources.placeholder
import diaguard.shared.generated.resources.week

@Composable
fun AverageDashboardItem(
    data: DashboardViewState.Average?,
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
                    Text(data?.day?.value ?: getString(Res.string.placeholder))
                }
                Row {
                    Text(
                        text = getString(Res.string.week),
                        modifier = Modifier.weight(1f),
                    )
                    Text(data?.week?.value ?: getString(Res.string.placeholder))
                }
                Row {
                    Text(
                        text = getString(Res.string.month),
                        modifier = Modifier.weight(1f),
                    )
                    Text(data?.month?.value ?: getString(Res.string.placeholder))
                }
            }
        }
    }
}