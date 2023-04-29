package com.faltenreich.diaguard.shared.view

expect class BottomSheetState constructor() {

    val isVisible: Boolean

    suspend fun show()

    suspend fun hide()
}