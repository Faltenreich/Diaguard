package com.faltenreich.diaguard.statistic.daterange

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.statistic.StatisticIntent
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.day_next
import diaguard.shared.generated.resources.day_previous
import diaguard.shared.generated.resources.ic_chevron_back
import diaguard.shared.generated.resources.ic_chevron_forward
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun StatisticDateRangeBar(
    state: StatisticDateRangeState,
    onIntent: (StatisticIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(AppTheme.colors.scheme.surfaceContainerLow)
            .height(AppTheme.dimensions.size.TouchSizeLarge),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = { onIntent(StatisticIntent.MoveDateRangeBack) },
            modifier = Modifier
                .fillMaxHeight()
                .padding(all = AppTheme.dimensions.padding.P_2),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_chevron_back),
                contentDescription = stringResource(Res.string.day_previous),
                tint = AppTheme.colors.scheme.onSurface,
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(state.title)
            Text(
                text = state.subtitle,
                style = AppTheme.typography.bodySmall,
            )
        }

        IconButton(
            onClick = { onIntent(StatisticIntent.MoveDateRangeForward) },
            modifier = Modifier
                .fillMaxHeight()
                .padding(all = AppTheme.dimensions.padding.P_2),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_chevron_forward),
                contentDescription = stringResource(Res.string.day_next),
                tint = AppTheme.colors.scheme.onSurface,
            )
        }
    }
}