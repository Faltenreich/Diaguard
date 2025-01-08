package com.faltenreich.diaguard.dashboard.hba1c

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.dashboard.DashboardState
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.hba1c
import diaguard.shared.generated.resources.hba1c_estimation
import diaguard.shared.generated.resources.hba1c_latest
import diaguard.shared.generated.resources.placeholder
import kotlinx.coroutines.launch

@Composable
fun HbA1cDashboardItem(
    data: DashboardState.HbA1c?,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    Card(
        onClick = { scope.launch { data?.onClick?.invoke() } },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(all = AppTheme.dimensions.padding.P_3),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
        ) {
            Text(
                text = getString(Res.string.hba1c),
                style = AppTheme.typography.labelMedium,
            )
            Row {
                Text(
                    text = getString(Res.string.hba1c_latest),
                    modifier = Modifier.weight(1f),
                )
                Text(data?.latest?.value ?: getString(Res.string.placeholder))
            }
            Row {
                Text(
                    text = getString(Res.string.hba1c_estimation),
                    modifier = Modifier.weight(1f),
                )
                Text(data?.estimation?.value ?: getString(Res.string.placeholder))
            }
        }
    }
}