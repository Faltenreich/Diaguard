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
import androidx.compose.ui.text.style.TextAlign
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.dashboard.DashboardViewState
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun LatestDashboardItem(
    data: DashboardViewState.Revisit.BloodSugar,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        when (data) {
            null -> Text(
                text = stringResource(MR.strings.entry_first_description),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = AppTheme.dimensions.padding.P_3),
                textAlign = TextAlign.Center,
                style = AppTheme.typography.headlineMedium,
            )
            else -> Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = AppTheme.dimensions.padding.P_2),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = data.value,
                    style = AppTheme.typography.headlineLarge,
                )
                Row {
                    Text(data.dateTime)
                    Text(data.ago)
                }
            }
        }
    }
}