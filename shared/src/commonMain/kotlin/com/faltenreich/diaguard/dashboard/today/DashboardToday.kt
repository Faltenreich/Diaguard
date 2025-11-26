package com.faltenreich.diaguard.dashboard.today

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.hyper
import com.faltenreich.diaguard.resource.hypo
import com.faltenreich.diaguard.resource.measurements
import com.faltenreich.diaguard.resource.placeholder
import com.faltenreich.diaguard.resource.today
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DashboardToday(
    state: DashboardTodayState?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
    ) {
        Box(modifier = Modifier.padding(all = AppTheme.dimensions.padding.P_3)) {
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3)) {
                Text(
                    text = stringResource(Res.string.today),
                    style = AppTheme.typography.labelMedium,
                )
                Row {
                    Text(
                        text = stringResource(Res.string.measurements),
                        modifier = Modifier.weight(1f),
                    )
                    Text(state?.totalCount?.toString() ?: stringResource(Res.string.placeholder))
                }
                Row {
                    Text(
                        text = stringResource(Res.string.hypo),
                        modifier = Modifier.weight(1f),
                    )
                    Text(state?.hypoCount?.toString() ?: stringResource(Res.string.placeholder))
                }
                Row {
                    Text(
                        text = stringResource(Res.string.hyper),
                        modifier = Modifier.weight(1f),
                    )
                    Text(state?.hyperCount?.toString() ?: stringResource(Res.string.placeholder))
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    DashboardToday(
        state = DashboardTodayState(
            totalCount = 10,
            hypoCount = 2,
            hyperCount = 5,
        ),
        onClick = {},
    )
}