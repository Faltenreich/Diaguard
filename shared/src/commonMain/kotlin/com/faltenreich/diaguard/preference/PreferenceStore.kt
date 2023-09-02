package com.faltenreich.diaguard.preference

import com.faltenreich.diaguard.preference.list.usecase.StartScreen
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.keyvalue.KeyValueStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenceStore(
    private val keyValueStore: KeyValueStore = inject(),
) {

    val startScreen: Flow<StartScreen>
        get() = keyValueStore.read<Int>(KEY_START_SCREEN).map { stableId ->
            stableId?.let(StartScreen::valueOf) ?: StartScreen.DASHBOARD
        }

    suspend fun setStartScreen(startScreen: StartScreen) {
        keyValueStore.write(KEY_START_SCREEN, startScreen.stableId)
    }

    companion object {

        private const val KEY_START_SCREEN = "startScreen"
    }
}