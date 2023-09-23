package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

class BottomSheetState {

    var delegate: SheetState = SheetState(skipPartiallyExpanded = true)

    val isVisible: Boolean
        get() = delegate.isVisible

    suspend fun show() {
        delegate.show()
    }

    suspend fun hide() {
        delegate.hide()
    }

    companion object {

        val Saver: Saver<BottomSheetState, *> = Saver(
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

@Composable
fun rememberBottomSheetState(): BottomSheetState {
    return rememberSaveable(saver = BottomSheetState.Saver) {
        BottomSheetState()
    }
}