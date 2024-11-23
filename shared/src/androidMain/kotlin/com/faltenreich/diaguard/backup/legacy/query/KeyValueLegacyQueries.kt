package com.faltenreich.diaguard.backup.legacy.query

import com.faltenreich.diaguard.backup.legacy.LegacyPreference
import com.faltenreich.diaguard.backup.legacy.LegacyPreferenceKey
import com.faltenreich.diaguard.shared.keyvalue.KeyValueStore
import com.faltenreich.diaguard.shared.keyvalue.read
import kotlinx.coroutines.flow.first

class KeyValueLegacyQueries(
    @PublishedApi internal val keyValueStore: KeyValueStore,
) {

    suspend inline fun <reified T: Any> getPreference(key: String): T? {
        return keyValueStore.read<T>(key).first()
    }

    suspend fun getPreferences(): List<LegacyPreference> {
        val keys = LegacyPreferenceKey.entries
        val preferences = keys.mapNotNull { key ->
            when (key.kClass) {
                Boolean::class -> {
                    val value = getPreference<Boolean>(key.key) ?: return@mapNotNull null
                    LegacyPreference.Boolean(
                        key = key,
                        value = value,
                    )
                }
                Int::class -> {
                    val value = getPreference<Int>(key.key) ?: return@mapNotNull null
                    LegacyPreference.Int(
                        key = key,
                        value = value,
                    )
                }
                Long::class -> {
                    val value = getPreference<Long>(key.key) ?: return@mapNotNull null
                    LegacyPreference.Long(
                        key = key,
                        value = value,
                    )
                }
                Float::class -> {
                    val value = getPreference<Float>(key.key) ?: return@mapNotNull null
                    LegacyPreference.Float(
                        key = key,
                        value = value,
                    )
                }
                String::class -> {
                    val value = getPreference<String>(key.key) ?: return@mapNotNull null
                    LegacyPreference.String(
                        key = key,
                        value = value,
                    )
                }
                else -> null
            }
        }
        return preferences
    }
}