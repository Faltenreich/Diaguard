package com.faltenreich.diaguard.timeline.date

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.day_next
import diaguard.shared.generated.resources.day_previous
import diaguard.shared.generated.resources.ic_chevron_back
import diaguard.shared.generated.resources.ic_chevron_forward
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TimelineDateBar(
    label: String,
    onBack: () -> Unit,
    onForward: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(AppTheme.colors.scheme.surfaceContainerLow)
            .fillMaxWidth()
            .padding(AppTheme.dimensions.padding.P_2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBack) {
            Icon(
                painter = painterResource(Res.drawable.ic_chevron_back),
                contentDescription = stringResource(Res.string.day_previous),
                tint = AppTheme.colors.scheme.onSurface,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = label,
            color = AppTheme.colors.scheme.onSurface,
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = onForward) {
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
    TimelineDateBar(
        label = now().toString(),
        onBack = {},
        onForward = {},
    )
}