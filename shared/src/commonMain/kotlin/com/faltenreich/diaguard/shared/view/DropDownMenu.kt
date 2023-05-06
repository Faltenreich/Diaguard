package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import com.faltenreich.diaguard.AppTheme

@Composable
expect fun DropDownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(
        x = AppTheme.dimensions.padding.P_0,
        y = AppTheme.dimensions.padding.P_0,
    ),
    content: @Composable ColumnScope.() -> Unit,
)