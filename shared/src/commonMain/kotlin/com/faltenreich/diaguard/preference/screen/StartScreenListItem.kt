package com.faltenreich.diaguard.preference.screen

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
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.core.localization.getString
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StartScreenListItem(
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
            painter = painterResource(startScreen.iconResource),
            contentDescription = null,
            modifier = Modifier.size(AppTheme.dimensions.padding.P_4),
        )
        Text(
            text = getString(startScreen.labelResource),
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
private fun Preview() = AppPreview {
    StartScreenListItem(
        startScreen = StartScreen.DASHBOARD,
        isSelected = true,
        onClick = {},
    )
}