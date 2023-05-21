package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.saveable.Saver

actual class BottomSheetState {

    actual val isVisible: Boolean
        get() = TODO("Not yet implemented")

    actual suspend fun show() {
        TODO("Not yet implemented")
    }

    actual suspend fun hide() {
        TODO("Not yet implemented")
    }

    actual companion object {

        actual val Saver: Saver<BottomSheetState, *>
            get() = TODO("Not yet implemented")

    }
}