package com.faltenreich.diaguard.dashboard.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.localization.getString

@Composable
fun LatestDashboardItem(
    data: DashboardViewState.Revisit.LatestBloodSugar?,
    onClick: (Entry?) -> Unit,
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
                    text = data?.value ?: getString(MR.strings.placeholder),
                    style = AppTheme.typography.displayLarge,
                    color = data?.tint?.getColor() ?: AppTheme.colors.scheme.onPrimary,
                )
                Text(
                    text = data?.timePassed ?: getString(MR.strings.entry_first_description),
                    style = AppTheme.typography.bodyMedium,
                )
            }
        }
    }
}