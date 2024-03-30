package com.faltenreich.diaguard.preference.store.screen

import com.faltenreich.diaguard.preference.Preference
import com.faltenreich.diaguard.preference.store.PreferenceStore
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetStartScreenUseCase(
    private val preferenceStore: PreferenceStore = inject(),
) {

    operator fun invoke(): Flow<StartScreen> {
        return preferenceStore.read(Preference.StartScreen).map {stableId ->
            stableId?.let(StartScreen::valueOf) ?: StartScreen.DASHBOARD
        }
    }
}