package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

expect class BottomSheetState constructor() {

    val isVisible: Boolean

    suspend fun show()

    suspend fun hide()

    companion object {

        val Saver: Saver<BottomSheetState, *>
    }
}

@Composable
fun rememberBottomSheetState(): BottomSheetState {
    return rememberSaveable(saver = BottomSheetState.Saver) {
        BottomSheetState()
    }
}