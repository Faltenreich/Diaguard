package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme

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
            .background(AppTheme.colors.scheme.surfaceVariant)
            .padding(all = AppTheme.dimensions.padding.P_3),
    )
}