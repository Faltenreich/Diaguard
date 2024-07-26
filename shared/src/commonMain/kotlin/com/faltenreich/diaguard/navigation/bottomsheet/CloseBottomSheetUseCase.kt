package com.faltenreich.diaguard.navigation.bottomsheet

import com.faltenreich.diaguard.navigation.Navigation

class CloseBottomSheetUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke() {
        navigation.popBottomSheet()
    }
}