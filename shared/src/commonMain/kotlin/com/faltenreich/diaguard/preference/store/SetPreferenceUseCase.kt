package com.faltenreich.diaguard.preference.store

import com.faltenreich.diaguard.preference.Preference

class SetPreferenceUseCase(
    @PublishedApi internal val preferenceStore: PreferenceStore,
) {

    suspend inline operator fun <reified Store, Domain> invoke(
        preference: Preference<Store, Domain>,
        value: Domain,
    ) {
        preferenceStore.write(preference, preference.onWrite(value))
    }
}