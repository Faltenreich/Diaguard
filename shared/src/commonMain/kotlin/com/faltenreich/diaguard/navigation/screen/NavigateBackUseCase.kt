package com.faltenreich.diaguard.navigation.screen

import com.faltenreich.diaguard.navigation.Navigation

class NavigateBackUseCase(
    private val navigation: Navigation,
) {

    suspend operator fun invoke(result: Pair<String, Any>? = null) {
        navigation.popScreen(result)
    }
}