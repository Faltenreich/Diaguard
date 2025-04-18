package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme

@Composable
fun FormRow(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = AppTheme.dimensions.size.TouchSizeMedium)
            .padding(
                horizontal = AppTheme.dimensions.padding.P_3,
                vertical = AppTheme.dimensions.padding.P_2,
            ),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon?.invoke()
        content()
    }
}