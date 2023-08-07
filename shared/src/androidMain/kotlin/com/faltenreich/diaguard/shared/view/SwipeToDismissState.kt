package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue

actual class SwipeToDismissState {

    var delegate: DismissState = DismissState(
        initialValue = DismissValue.Default,
        confirmValueChange = { true },
    )

    actual fun isDismissed(): Boolean {
        return delegate.isDismissed(DismissDirection.StartToEnd) ||
            delegate.isDismissed(DismissDirection.EndToStart)
    }
}