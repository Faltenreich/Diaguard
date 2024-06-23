package com.faltenreich.diaguard.navigation

class OpenBottomSheetUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke(screen: Screen) {
        navigation.pushBottomSheet(screen)
    }
}