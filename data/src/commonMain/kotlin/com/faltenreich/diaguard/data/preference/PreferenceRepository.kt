package com.faltenreich.diaguard.data.preference

import com.faltenreich.diaguard.localization.Localization
import kotlinx.coroutines.flow.Flow

class PreferenceRepository internal constructor(
    @PublishedApi internal val dao: PreferenceDao,
    private val localization: Localization,
) {

    @PublishedApi internal fun <Store, Domain> getKey(preference: Preference<Store, Domain>): String {
        // FIXME: java.lang.IllegalArgumentException: Invalid symbol '_'(137) at index 10
        val key = localization.getString(preference.key).takeIf(String::isNotBlank)
        checkNotNull(key)
        return key
    }

    inline fun <reified Store, Domain> read(preference: Preference<Store, Domain>): Flow<Store?> {
        return dao.read(getKey(preference))
    }

    suspend inline fun <reified Store: Any, Domain> write(preference: Preference<Store, Domain>, value: Store) {
        dao.write(getKey(preference), value)
    }
}