package com.faltenreich.diaguard.dashboard.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.shared.localization.getString

@Composable
fun HbA1cDashboardItem(
    data: DashboardViewState.Revisit.HbA1c?,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(all = AppTheme.dimensions.padding.P_3)) {
            Text(
                text = getString(MR.strings.hba1c),
                style = AppTheme.typography.labelMedium,
            )
            // TODO
            Text(data.toString())
        }
    }
}