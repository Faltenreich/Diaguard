package com.faltenreich.diaguard.preference.list.item.screen

import com.faltenreich.diaguard.preference.PreferenceStore
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow

class GetStartScreenUseCase(
    private val preferenceStore: PreferenceStore = inject(),
) {

    operator fun invoke(): Flow<StartScreen> {
        return preferenceStore.startScreen
    }
}