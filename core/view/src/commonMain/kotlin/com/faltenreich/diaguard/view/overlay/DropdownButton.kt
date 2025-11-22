package com.faltenreich.diaguard.view.overlay

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DropdownButton(
    text: String,
    items: List<Pair<String, () -> Unit>>,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .clickable { expanded = true }
                .defaultMinSize(minHeight = ButtonDefaults.MinHeight)
                .fillMaxWidth()
                .padding(AppTheme.dimensions.padding.P_3),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text)
            subtitle?.let { subtitle ->
                Text(
                    text = subtitle,
                    style = AppTheme.typography.bodySmall,
                )
            }
        }
        DropdownTextMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            items = items,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    DropdownButton(
        text = "Text",
        items = emptyList(),
        subtitle = "Subtitle",
    )
}