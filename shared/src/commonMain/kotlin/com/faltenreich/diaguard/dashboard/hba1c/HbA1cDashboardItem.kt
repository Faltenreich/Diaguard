package com.faltenreich.diaguard.dashboard.hba1c

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.dashboard.DashboardState
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.hba1c

@Composable
fun HbA1cDashboardItem(
    data: DashboardState.HbA1c?,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(all = AppTheme.dimensions.padding.P_3)) {
            Text(
                text = getString(Res.string.hba1c),
                style = AppTheme.typography.labelMedium,
            )
            // TODO
            Text(data.toString())
        }
    }
}