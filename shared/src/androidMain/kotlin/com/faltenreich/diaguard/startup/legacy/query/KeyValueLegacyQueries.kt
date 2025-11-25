package com.faltenreich.diaguard.startup.legacy.query

import com.faltenreich.diaguard.data.preference.Preference
import com.faltenreich.diaguard.preference.color.ColorScheme
import com.faltenreich.diaguard.preference.color.ColorSchemePreference
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.food.ShowBrandedFoodPreference
import com.faltenreich.diaguard.preference.food.ShowCommonFoodPreference
import com.faltenreich.diaguard.preference.food.ShowCustomFoodPreference
import com.faltenreich.diaguard.preference.screen.StartScreen
import com.faltenreich.diaguard.preference.screen.StartScreenPreference
import com.faltenreich.diaguard.preference.version.VersionCodePreference
import com.faltenreich.diaguard.persistence.keyvalue.KeyValueStore
import com.faltenreich.diaguard.persistence.keyvalue.read
import kotlinx.coroutines.flow.first

class KeyValueLegacyQueries(
    @PublishedApi internal val keyValueStore: KeyValueStore,
) {

    suspend fun hasPreferences(): Boolean {
        return keyValueStore.exists()
    }

    private suspend inline fun <reified T: Any> getPreference(key: String): T? {
        return keyValueStore.read<T>(key).first()
    }

    suspend fun <Store, Domain> getPreference(preference: Preference<Store, Domain>): Domain? {
        @Suppress("UNCHECKED_CAST")
        return when (preference) {
            is ColorSchemePreference -> getColorScheme()
            is DecimalPlacesPreference -> getPreference<Int>("Decimal places")
            is ShowBrandedFoodPreference -> getPreference<Boolean>("showBrandedFood")
            is ShowCommonFoodPreference -> getPreference<Boolean>("showCommonFood")
            is ShowCustomFoodPreference -> getPreference<Boolean>("showCustomFood")
            is StartScreenPreference -> getStartScreen()
            is VersionCodePreference -> getPreference<Int>("versionCode")
            else -> error("Requesting unhandled preference: $preference")
        } as? Domain
    }

    private suspend fun getColorScheme(): ColorScheme? {
        return getPreference<String>("theme")?.let { value ->
            when (value) {
                "0" -> ColorScheme.LIGHT
                "1" -> ColorScheme.DARK
                "2" -> ColorScheme.SYSTEM // was time-based which is now unsupported
                "3" -> ColorScheme.SYSTEM
                else -> null
            }
        }
    }

    private suspend fun getStartScreen(): StartScreen? {
        return getPreference<String>("startscreen")?.let { value ->
            when (value) {
                "0" -> StartScreen.DASHBOARD
                "1" -> StartScreen.TIMELINE
                "2" -> StartScreen.LOG
                else -> null
            }
        }
    }
}