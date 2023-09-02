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
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.dashboard.DashboardViewState
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun TodayDashboardItem(
    data: DashboardViewState.Revisit.Today?,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Box(modifier = Modifier.padding(all = AppTheme.dimensions.padding.P_3)) {
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3)) {
                Text(
                    text = stringResource(MR.strings.today),
                    color = AppTheme.colors.Green,
                )
                Row {
                    Text(
                        text = stringResource(MR.strings.measurements),
                        modifier = Modifier.weight(1f),
                    )
                    Text(data?.totalCount?.toString() ?: stringResource(MR.strings.placeholder))
                }
                Row {
                    Text(
                        text = stringResource(MR.strings.hyper),
                        modifier = Modifier.weight(1f),
                    )
                    Text(data?.hyperCount?.toString() ?: stringResource(MR.strings.placeholder))
                }
                Row {
                    Text(
                        text = stringResource(MR.strings.hypo),
                        modifier = Modifier.weight(1f),
                    )
                    Text(data?.hypoCount?.toString() ?: stringResource(MR.strings.placeholder))
                }
            }
        }
    }
}