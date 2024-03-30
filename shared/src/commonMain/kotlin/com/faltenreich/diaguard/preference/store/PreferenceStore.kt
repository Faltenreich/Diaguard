package com.faltenreich.diaguard.preference.store

import com.faltenreich.diaguard.preference.Preference
import com.faltenreich.diaguard.shared.keyvalue.KeyValueStore
import com.faltenreich.diaguard.shared.localization.Localization
import dev.icerock.moko.resources.StringResource
import kotlinx.coroutines.flow.Flow

// TODO: Migrate preferences
class PreferenceStore(
    @PublishedApi internal val keyValueStore: KeyValueStore,
    private val localization: Localization,
) {

    @PublishedApi internal fun getKey(resource: StringResource, vararg arguments: Any): String {
        val key = localization.getString(resource, arguments).takeIf(String::isNotBlank)
        requireNotNull(key)
        return key
    }

    inline fun <reified T> read(preference: Preference<T>): Flow<T?> {
        return keyValueStore.read<T>(getKey(preference.key))
    }

    suspend inline fun <reified T> write(preference: Preference<T>, value: T) {
        keyValueStore.write(getKey(preference.key), value)
    }
}