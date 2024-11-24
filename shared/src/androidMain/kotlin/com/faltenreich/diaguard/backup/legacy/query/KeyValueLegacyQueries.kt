package com.faltenreich.diaguard.backup.legacy.query

import com.faltenreich.diaguard.preference.Preference
import com.faltenreich.diaguard.preference.color.ColorScheme
import com.faltenreich.diaguard.preference.color.ColorSchemePreference
import com.faltenreich.diaguard.shared.keyvalue.KeyValueStore
import com.faltenreich.diaguard.shared.keyvalue.read
import kotlinx.coroutines.flow.first

class KeyValueLegacyQueries(
    @PublishedApi internal val keyValueStore: KeyValueStore,
) {

    suspend inline fun <reified T: Any> getPreference(key: String): T? {
        return keyValueStore.read<T>(key).first()
    }

    suspend fun <Store, Domain> getPreference(preference: Preference<Store, Domain>): Domain? {
        return when (preference) {
            is ColorSchemePreference -> getPreference<String>("theme")?.let { value ->
                when (value) {
                    "0" -> ColorScheme.LIGHT
                    "1" -> ColorScheme.DARK
                    else -> ColorScheme.SYSTEM
                }
            }
            else -> throw IllegalStateException("Requesting unhandled preference: $preference")
        } as? Domain
    }
}