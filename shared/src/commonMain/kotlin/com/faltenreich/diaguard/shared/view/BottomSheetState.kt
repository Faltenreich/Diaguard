package com.faltenreich.diaguard.shared.view

expect class BottomSheetState {

    val isVisible: Boolean

    suspend fun show()

    suspend fun hide()
}