package com.faltenreich.diaguard.statistic.daterange

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.view.theme.AppTheme
import com.faltenreich.diaguard.data.preview.AppPreview
import com.faltenreich.diaguard.statistic.StatisticIntent
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.day_next
import diaguard.shared.generated.resources.day_previous
import diaguard.shared.generated.resources.ic_chevron_back
import diaguard.shared.generated.resources.ic_chevron_forward
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StatisticDateRangeBar(
    state: StatisticDateRangeState,
    onIntent: (StatisticIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(AppTheme.colors.scheme.surfaceContainerLow)
            .fillMaxWidth()
            .padding(AppTheme.dimensions.padding.P_2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = { onIntent(StatisticIntent.MoveDateRangeBack) }) {
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

        IconButton(onClick = { onIntent(StatisticIntent.MoveDateRangeForward) }) {
            Icon(
                painter = painterResource(Res.drawable.ic_chevron_forward),
                contentDescription = stringResource(Res.string.day_next),
                tint = AppTheme.colors.scheme.onSurface,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    StatisticDateRangeBar(
        state = StatisticDateRangeState(
            type = StatisticDateRangeType.WEEK,
            dateRange = today() .. today(),
            title = "title",
            subtitle = "subtitle",
        ),
        onIntent = {},
    )
}