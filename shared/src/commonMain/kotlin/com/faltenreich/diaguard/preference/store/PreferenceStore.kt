package com.faltenreich.diaguard.preference.store

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.preference.store.color.ColorScheme
import com.faltenreich.diaguard.preference.store.screen.StartScreen
import com.faltenreich.diaguard.shared.keyvalue.KeyValueStore
import com.faltenreich.diaguard.shared.localization.Localization
import dev.icerock.moko.resources.StringResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// TODO: Migrate preferences
class PreferenceStore(
    private val keyValueStore: KeyValueStore,
    private val localization: Localization,
) {

    private fun getKey(resource: StringResource, vararg arguments: Any): String {
        val key = localization.getString(resource, arguments).takeIf(String::isNotBlank)
        requireNotNull(key)
        return key
    }

    fun getStartScreen(): Flow<StartScreen> {
        return keyValueStore.read<Int>(getKey(MR.strings.preference_start_screen)).map { stableId ->
            stableId?.let(StartScreen::valueOf) ?: StartScreen.DASHBOARD
        }
    }

    suspend fun setStartScreen(startScreen: StartScreen) {
        keyValueStore.write(getKey(MR.strings.preference_start_screen), startScreen.stableId)
    }

    fun getColorScheme(): Flow<ColorScheme> {
        return keyValueStore.read<Int>(getKey(MR.strings.preference_theme)).map { stableId ->
            stableId?.let(ColorScheme::valueOf) ?: ColorScheme.SYSTEM
        }
    }

    suspend fun setColorScheme(colorScheme: ColorScheme) {
        keyValueStore.write(getKey(MR.strings.preference_theme), colorScheme.stableId)
    }
}