package com.faltenreich.diaguard.data.preference

import kotlinx.coroutines.flow.Flow

class PreferenceRepository internal constructor(
    @PublishedApi internal val dao: PreferenceDao,
) {

    // FIXME: Pass default value to SharedPreferences (and DataStore?)
    inline fun <reified Store, Domain> read(preference: Preference<Store, Domain>): Flow<Store?> {
        return dao.read(preference.key)
    }

    suspend inline fun <reified Store: Any, Domain> write(preference: Preference<Store, Domain>, value: Store) {
        dao.write(preference.key, value)
    }
}