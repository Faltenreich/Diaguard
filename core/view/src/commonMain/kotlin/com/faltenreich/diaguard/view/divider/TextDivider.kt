package com.faltenreich.diaguard.view.divider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.faltenreich.diaguard.view.theme.AppTheme

@Composable
fun TextDivider(
    label: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = label,
        style = AppTheme.typography.bodyMedium,
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colors.scheme.surfaceContainerLow)
            .padding(all = AppTheme.dimensions.padding.P_3),
    )
}

@Preview
@Composable
private fun Preview() {
    TextDivider(label = "Label")
}