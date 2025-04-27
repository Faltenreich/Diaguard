package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.faltenreich.diaguard.AppTheme

@Composable
fun TextCheckbox(
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.toggleable(
            value = checked,
            role = Role.Checkbox,
            onValueChange = onCheckedChange,
        ),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title)
            subtitle?.let {
                Text(
                    text = subtitle,
                    style = AppTheme.typography.bodySmall,
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = null,
        )
    }
}