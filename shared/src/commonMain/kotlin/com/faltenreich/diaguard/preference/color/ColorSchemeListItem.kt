package com.faltenreich.diaguard.preference.color

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.preference.ColorScheme
import com.faltenreich.diaguard.shared.localization.getString

@Composable
fun ColorSchemeListItem(
    colorScheme: ColorScheme,
    selection: ColorScheme,
    onClick: (ColorScheme) -> Unit,
) {
    Row(
        modifier = Modifier
            .selectable(
                selected = selection == colorScheme,
                onClick = { onClick(colorScheme) },
                role = Role.RadioButton,
            )
            .padding(all = AppTheme.dimensions.padding.P_3_5),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = getString(colorScheme.labelResource),
            modifier = Modifier.weight(1f),
        )
        RadioButton(
            selected = selection == colorScheme,
            onClick = null,
        )
    }
}