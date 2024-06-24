package com.faltenreich.diaguard.navigation

class CloseBottomSheetUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke() {
        navigation.popBottomSheet()
    }
}