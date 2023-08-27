package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme

@Composable
fun FormRowLabel(
    label: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_1),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Divider(modifier = Modifier.width(AppTheme.dimensions.padding.P_3_5))
        Text(
            text = label,
            style = AppTheme.typography.bodySmall,
        )
        Divider(modifier = Modifier.weight(1f))
    }
}