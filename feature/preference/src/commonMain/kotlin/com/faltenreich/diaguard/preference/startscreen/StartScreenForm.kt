package com.faltenreich.diaguard.preference.startscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.data.preference.startscreen.StartScreen
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.view.theme.AppTheme
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.start_screen
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun StartScreenForm(
    state: StartScreen,
    onChange: (StartScreen) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.start_screen),
            style = AppTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_2))

        Column(modifier = Modifier.selectableGroup()) {
            StartScreen.entries.forEach { startScreen ->
                StartScreenListItem(
                    startScreen = startScreen,
                    isSelected = state == startScreen,
                    onClick = { onChange(startScreen) },
                )
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    StartScreenForm(
        state = StartScreen.DASHBOARD,
        onChange = {},
    )
}