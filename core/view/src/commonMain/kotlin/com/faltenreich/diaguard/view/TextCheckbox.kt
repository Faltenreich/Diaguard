package com.faltenreich.diaguard.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TextCheckbox(
    title: String,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    Row(
        modifier = modifier,
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
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    TextCheckbox(
        title = "Title",
        subtitle = "Subtitle",
        checked = true,
        onCheckedChange = {},
    )
}