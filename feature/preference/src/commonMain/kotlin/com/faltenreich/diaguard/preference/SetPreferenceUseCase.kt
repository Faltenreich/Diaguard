package com.faltenreich.diaguard.preference

import com.faltenreich.diaguard.data.preference.Preference
import com.faltenreich.diaguard.data.preference.PreferenceRepository

class SetPreferenceUseCase(@PublishedApi internal val repository: PreferenceRepository) {

    suspend inline operator fun <reified Store: Any, Domain> invoke(
        preference: Preference<Store, Domain>,
        value: Domain,
    ) {
        repository.write(preference, preference.onWrite(value))
    }
}