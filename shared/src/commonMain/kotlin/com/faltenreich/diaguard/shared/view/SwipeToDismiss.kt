package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun SwipeToDismiss(
    state: SwipeToDismissState,
    background: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
)

expect class SwipeToDismissState
expect fun SwipeToDismissState.isDismissed(): Boolean

@Composable
expect fun rememberSwipeToDismissState(): SwipeToDismissState