package com.faltenreich.diaguard.preference.startscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.faltenreich.diaguard.data.preference.startscreen.StartScreen
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.view.theme.AppTheme
import diaguard.feature.preference.generated.resources.Res
import diaguard.feature.preference.generated.resources.dashboard
import diaguard.feature.preference.generated.resources.ic_dashboard
import diaguard.feature.preference.generated.resources.ic_log
import diaguard.feature.preference.generated.resources.ic_timeline
import diaguard.feature.preference.generated.resources.log
import diaguard.feature.preference.generated.resources.timeline
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun StartScreenListItem(
    startScreen: StartScreen,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .selectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.RadioButton,
            )
            .padding(all = AppTheme.dimensions.padding.P_3_5),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3_5),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(
                when (startScreen) {
                    StartScreen.DASHBOARD -> Res.drawable.ic_dashboard
                    StartScreen.TIMELINE -> Res.drawable.ic_timeline
                    StartScreen.LOG -> Res.drawable.ic_log
                }
            ),
            contentDescription = null,
            modifier = Modifier.size(AppTheme.dimensions.padding.P_4),
        )
        Text(
            text = stringResource(
                when (startScreen) {
                    StartScreen.DASHBOARD -> Res.string.dashboard
                    StartScreen.TIMELINE -> Res.string.timeline
                    StartScreen.LOG -> Res.string.log
                }
            ),
            modifier = Modifier.weight(1f),
        )
        RadioButton(
            selected = isSelected,
            onClick = null,
        )
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    StartScreenListItem(
        startScreen = StartScreen.DASHBOARD,
        isSelected = true,
        onClick = {},
    )
}