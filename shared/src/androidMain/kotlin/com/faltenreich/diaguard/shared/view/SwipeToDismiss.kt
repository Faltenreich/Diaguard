package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun SwipeToDismiss(
    state: SwipeToDismissState,
    background: @Composable RowScope.() -> Unit,
    modifier: Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    androidx.compose.material3.SwipeToDismiss(
        state = state.delegate,
        background = background,
        dismissContent = content,
        modifier = modifier,
    )
}