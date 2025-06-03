package com.faltenreich.diaguard.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import com.faltenreich.diaguard.AppTheme
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.date_picker_open
import diaguard.shared.generated.resources.day_next
import diaguard.shared.generated.resources.day_previous
import diaguard.shared.generated.resources.ic_chevron_back
import diaguard.shared.generated.resources.ic_chevron_forward
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TimelineDateBar(
    label: String,
    onIntent: (TimelineIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(AppTheme.colors.scheme.surfaceContainerLow)
            .fillMaxWidth()
            .padding(AppTheme.dimensions.padding.P_2),
    ) {
        IconButton(onClick = { onIntent(TimelineIntent.MoveDayBack) }) {
            Icon(
                painter = painterResource(Res.drawable.ic_chevron_back),
                contentDescription = stringResource(Res.string.day_previous),
                tint = AppTheme.colors.scheme.onSurface,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        val onClickLabel = stringResource(Res.string.date_picker_open)
        TextButton(
            onClick = { onIntent(TimelineIntent.OpenDateDialog) },
            modifier = Modifier.semantics {
                onClick(
                    label = onClickLabel,
                    action = { onIntent(TimelineIntent.OpenDateDialog); true },
                )
            },
        ) {
            Text(
                text = label,
                color = AppTheme.colors.scheme.onSurface,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { onIntent(TimelineIntent.MoveDayForward) }) {
            Icon(
                painter = painterResource(Res.drawable.ic_chevron_forward),
                contentDescription = stringResource(Res.string.day_next),
                tint = AppTheme.colors.scheme.onSurface,
            )
        }
    }
}