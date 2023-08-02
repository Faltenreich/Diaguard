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
    TODO("Not yet implemented")
}

actual class SwipeToDismissState

actual fun SwipeToDismissState.isDismissed(): Boolean {
    TODO("Not yet implemented")
}

@Composable
actual fun rememberSwipeToDismissState(): SwipeToDismissState {
    TODO("Not yet implemented")
}