package com.faltenreich.diaguard.preference.store

import com.faltenreich.diaguard.preference.Preference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPreferenceUseCase(
    @PublishedApi internal val preferenceStore: PreferenceStore,
) {

    inline operator fun <reified Store, Domain> invoke(preference: Preference<Store, Domain>): Flow<Domain> {
        return preferenceStore.read(preference).map { store ->
            store?.let { preference.fromStore(store) } ?: preference.default
        }
    }
}