package com.faltenreich.diaguard.preference.store

import com.faltenreich.diaguard.data.preference.Preference
import com.faltenreich.diaguard.data.preference.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class GetPreferenceUseCase(@PublishedApi internal val repository: PreferenceRepository) {

    inline operator fun <reified Store, Domain> invoke(preference: Preference<Store, Domain>): Flow<Domain> {
        return repository.read(preference)
            .map { store ->
                store?.let { preference.onRead(store) }
                    ?: preference.default
            }
            .distinctUntilChanged()
    }
}