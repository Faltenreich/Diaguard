package com.faltenreich.diaguard.preference.store

import com.faltenreich.diaguard.preference.Preference
import com.faltenreich.diaguard.shared.keyvalue.KeyValueStore
import com.faltenreich.diaguard.shared.keyvalue.read
import com.faltenreich.diaguard.shared.keyvalue.write
import com.faltenreich.diaguard.shared.localization.Localization
import kotlinx.coroutines.flow.Flow

// TODO: Migrate preferences
class PreferenceStore(
    @PublishedApi internal val keyValueStore: KeyValueStore,
    private val localization: Localization,
) {

    @PublishedApi internal fun <Store, Domain> getKey(preference: Preference<Store, Domain>): String {
        // FIXME: java.lang.IllegalArgumentException: Invalid symbol '_'(137) at index 10
        val key = localization.getString(preference.key).takeIf(String::isNotBlank)
        requireNotNull(key)
        return key
    }

    inline fun <reified Store, Domain> read(preference: Preference<Store, Domain>): Flow<Store?> {
        return keyValueStore.read(getKey(preference))
    }

    suspend inline fun <reified Store: Any, Domain> write(preference: Preference<Store, Domain>, value: Store) {
        keyValueStore.write(getKey(preference), value)
    }
}