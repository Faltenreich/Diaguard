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
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.core.localization.getString
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.hyper
import diaguard.shared.generated.resources.hypo
import diaguard.shared.generated.resources.measurements
import diaguard.shared.generated.resources.placeholder
import diaguard.shared.generated.resources.today
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
                    text = getString(Res.string.today),
                    style = AppTheme.typography.labelMedium,
                )
                Row {
                    Text(
                        text = getString(Res.string.measurements),
                        modifier = Modifier.weight(1f),
                    )
                    Text(state?.totalCount?.toString() ?: getString(Res.string.placeholder))
                }
                Row {
                    Text(
                        text = getString(Res.string.hypo),
                        modifier = Modifier.weight(1f),
                    )
                    Text(state?.hypoCount?.toString() ?: getString(Res.string.placeholder))
                }
                Row {
                    Text(
                        text = getString(Res.string.hyper),
                        modifier = Modifier.weight(1f),
                    )
                    Text(state?.hyperCount?.toString() ?: getString(Res.string.placeholder))
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    DashboardToday(
        state = DashboardTodayState(
            totalCount = 10,
            hypoCount = 2,
            hyperCount = 5,
        ),
        onClick = {},
    )
}