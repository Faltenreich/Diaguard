package com.faltenreich.diaguard.preference.store.screen

import com.faltenreich.diaguard.preference.store.PreferenceStore
import com.faltenreich.diaguard.shared.di.inject

class SetStartScreenUseCase(
    private val preferenceStore: PreferenceStore = inject(),
) {

    suspend operator fun invoke(startScreen: StartScreen) {
        preferenceStore.setStartScreen(startScreen)
    }
}