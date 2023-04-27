package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.SheetState

actual class BottomSheetState(private val sheetState: SheetState = SheetState(skipPartiallyExpanded = true)) {

    actual val isVisible: Boolean
        get() = sheetState.isVisible

    actual suspend fun show() {
        sheetState.show()
    }

    actual suspend fun hide() {
        sheetState.hide()
    }
}