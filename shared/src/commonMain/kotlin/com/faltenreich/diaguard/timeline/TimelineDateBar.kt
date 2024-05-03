package com.faltenreich.diaguard.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_chevron_back
import diaguard.shared.generated.resources.ic_chevron_forward
import diaguard.shared.generated.resources.timeline
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TimelineDateBar(
    label: String,
    onIntent: (TimelineIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.background(AppTheme.colors.scheme.primaryContainer)) {
        IconButton(
            onClick = { onIntent(TimelineIntent.SelectPreviousDay) },
            modifier = Modifier
                .fillMaxHeight()
                .padding(all = AppTheme.dimensions.padding.P_2),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_chevron_back),
                contentDescription = stringResource(Res.string.timeline),
                tint = AppTheme.colors.scheme.onPrimary,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            onClick = { onIntent(TimelineIntent.SelectDate) },
            modifier = Modifier.fillMaxHeight(),
        ) {
            Text(
                text = label,
                color = AppTheme.colors.scheme.onPrimary,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { onIntent(TimelineIntent.SelectNextDay) },
            modifier = Modifier
                .fillMaxHeight()
                .padding(all = AppTheme.dimensions.padding.P_2),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_chevron_forward),
                contentDescription = stringResource(Res.string.timeline),
                tint = AppTheme.colors.scheme.onPrimary,
            )
        }
    }
}