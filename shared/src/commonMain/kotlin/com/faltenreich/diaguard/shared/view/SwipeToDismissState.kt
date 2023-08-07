package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

expect class SwipeToDismissState constructor() {

    fun isDismissed(): Boolean
}

@Composable
fun rememberSwipeToDismissState(): SwipeToDismissState {
    return remember { SwipeToDismissState() }
}