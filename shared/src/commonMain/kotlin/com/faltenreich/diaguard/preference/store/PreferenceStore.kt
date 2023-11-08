package com.faltenreich.diaguard.preference.store

import com.faltenreich.diaguard.preference.store.color.ColorScheme
import com.faltenreich.diaguard.preference.store.screen.StartScreen
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

    val colorScheme: Flow<ColorScheme>
        get() = keyValueStore.read<Int>(KEY_COLOR_SCHEME).map { stableId ->
            stableId?.let(ColorScheme::valueOf) ?: ColorScheme.SYSTEM
        }

    suspend fun setStartScreen(startScreen: StartScreen) {
        keyValueStore.write(KEY_START_SCREEN, startScreen.stableId)
    }

    suspend fun setColorScheme(colorScheme: ColorScheme) {
        keyValueStore.write(KEY_COLOR_SCHEME, colorScheme.stableId)
    }

    companion object {

        private const val KEY_START_SCREEN = "startScreen"
        private const val KEY_COLOR_SCHEME = "colorScheme"
    }
}