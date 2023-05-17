package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.saveable.Saver

actual class BottomSheetState {

    var delegate: SheetState = SheetState(skipPartiallyExpanded = true)

    actual val isVisible: Boolean
        get() = delegate.isVisible

    actual suspend fun show() {
        delegate.show()
    }

    actual suspend fun hide() {
        delegate.hide()
    }

    actual companion object {

        actual val Saver: Saver<BottomSheetState, *> = Saver(
            save = { it.isVisible },
            restore = { isVisible ->
                BottomSheetState().apply {
                    delegate = SheetState(
                        skipPartiallyExpanded = true,
                        initialValue = if (isVisible) SheetValue.Expanded else SheetValue.Hidden,
                    )
                }
            }
        )

    }
}