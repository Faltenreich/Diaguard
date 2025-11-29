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
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.day_next
import com.faltenreich.diaguard.resource.day_previous
import com.faltenreich.diaguard.resource.ic_chevron_back
import com.faltenreich.diaguard.resource.ic_chevron_forward
import com.faltenreich.diaguard.statistic.StatisticIntent
import com.faltenreich.diaguard.view.theme.AppTheme
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
private fun Preview() = PreviewScaffold {
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