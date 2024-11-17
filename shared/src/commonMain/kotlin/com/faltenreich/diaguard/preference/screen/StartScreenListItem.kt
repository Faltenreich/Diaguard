package com.faltenreich.diaguard.preference.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.preference.StartScreen
import com.faltenreich.diaguard.shared.localization.getString
import org.jetbrains.compose.resources.painterResource

@Composable
fun StartScreenListItem(
    startScreen: StartScreen,
    selection: StartScreen,
    onClick: (StartScreen) -> Unit,
) {
    Row(
        modifier = Modifier
            .selectable(
                selected = selection == startScreen,
                onClick = { onClick(startScreen) },
                role = Role.RadioButton,
            )
            .padding(all = AppTheme.dimensions.padding.P_3_5),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3_5),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(startScreen.iconResource),
            contentDescription = null,
            modifier = Modifier.size(AppTheme.dimensions.padding.P_4),
        )
        Text(
            text = getString(startScreen.labelResource),
            modifier = Modifier.weight(1f),
        )
        RadioButton(
            selected = selection == startScreen,
            onClick = null,
        )
    }
}