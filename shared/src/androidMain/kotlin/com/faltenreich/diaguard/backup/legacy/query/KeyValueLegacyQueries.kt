package com.faltenreich.diaguard.backup.legacy.query

import com.faltenreich.diaguard.backup.legacy.LegacyPreference
import com.faltenreich.diaguard.shared.keyvalue.KeyValueStore
import com.faltenreich.diaguard.shared.keyvalue.read
import kotlinx.coroutines.flow.first

class KeyValueLegacyQueries(
    private val keyValueStore: KeyValueStore,
) {

    private suspend inline fun <reified T: Any> getPreference(key: String): T? {
        return keyValueStore.read<T>(key).first()
    }

    suspend fun getPreferences(): List<LegacyPreference> {
        val preferences = listOf(
            "versionCode".let { key ->
                LegacyPreference.Int(
                    key = key,
                    // TODO: Handle null-values
                    value = getPreference(key)!!,
                )
            },
            "theme".let { key ->
                LegacyPreference.String(
                    key = key,
                    value = getPreference(key)!!,
                )
            },
            // TODO: Add all legacy preferences
        )
        return preferences
    }
}