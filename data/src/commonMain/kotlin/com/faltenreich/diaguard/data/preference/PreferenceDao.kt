package com.faltenreich.diaguard.data.preference

import com.faltenreich.diaguard.persistence.keyvalue.KeyValueStore
import com.faltenreich.diaguard.persistence.keyvalue.read
import com.faltenreich.diaguard.persistence.keyvalue.write
import kotlinx.coroutines.flow.Flow

@PublishedApi
internal class PreferenceDao(
    @PublishedApi internal val keyValueStore: KeyValueStore,
) {

    inline fun <reified T> read(key: String): Flow<T?> {
        // FIXME: Pass default value to SharedPreferences (and DataStore?)
        return keyValueStore.read(key)
    }

    suspend inline fun <reified T: Any> write(key: String, value: T) {
        keyValueStore.write(key, value)
    }
}