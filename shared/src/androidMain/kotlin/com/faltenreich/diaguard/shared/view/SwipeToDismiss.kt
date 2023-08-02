package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.rememberDismissState
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
        state = state,
        background = background,
        dismissContent = content,
        modifier = modifier,
    )
}

actual typealias SwipeToDismissState = DismissState

actual fun SwipeToDismissState.isDismissed(): Boolean {
    return isDismissed(DismissDirection.StartToEnd) ||
        isDismissed(DismissDirection.EndToStart)
}

@Composable
actual fun rememberSwipeToDismissState(): SwipeToDismissState {
    return rememberDismissState()
}