package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    Surface(
        onClick = { onCheckedChange(!checked) },
        shape = AppTheme.shapes.medium,
    ) {
        Row(
            modifier = modifier
                .defaultMinSize(minHeight = ButtonDefaults.MinHeight)
                .fillMaxWidth()
                .padding(all = AppTheme.dimensions.padding.P_3),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
            ) {
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
                onCheckedChange = onCheckedChange,
            )
        }
    }
}