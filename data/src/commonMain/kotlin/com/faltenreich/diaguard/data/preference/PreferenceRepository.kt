package com.faltenreich.diaguard.data.preference

import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.persistence.keyvalue.KeyValueStore
import com.faltenreich.diaguard.persistence.keyvalue.read
import com.faltenreich.diaguard.persistence.keyvalue.write
import kotlinx.coroutines.flow.Flow

// TODO: Migrate preferences
class PreferenceRepository(
    @PublishedApi internal val keyValueStore: KeyValueStore,
    private val localization: Localization,
) {

    @PublishedApi internal fun <Store, Domain> getKey(preference: Preference<Store, Domain>): String {
        // FIXME: java.lang.IllegalArgumentException: Invalid symbol '_'(137) at index 10
        val key = localization.getString(preference.key).takeIf(String::isNotBlank)
        checkNotNull(key)
        return key
    }

    inline fun <reified Store, Domain> read(preference: Preference<Store, Domain>): Flow<Store?> {
        // FIXME: Pass default value to SharedPreferences (and DataStore?)
        return keyValueStore.read(getKey(preference))
    }

    suspend inline fun <reified Store: Any, Domain> write(preference: Preference<Store, Domain>, value: Store) {
        keyValueStore.write(getKey(preference), value)
    }
}