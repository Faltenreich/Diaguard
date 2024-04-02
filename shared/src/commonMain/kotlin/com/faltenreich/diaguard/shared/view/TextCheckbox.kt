package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme

@Composable
fun TextCheckbox(
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = { onCheckedChange(!checked) },
        modifier = modifier.defaultMinSize(minHeight = ButtonDefaults.MinHeight),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
        ) {
            Text(
                text = title,
                style = AppTheme.typography.bodyMedium,
            )
            subtitle?.let {
                Text(
                    text = subtitle,
                    style = AppTheme.typography.bodySmall,
                )
            }
        }
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}