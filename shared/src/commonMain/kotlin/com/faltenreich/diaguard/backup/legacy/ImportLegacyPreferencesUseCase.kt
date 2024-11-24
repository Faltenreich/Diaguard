package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.preference.Preference
import com.faltenreich.diaguard.preference.color.ColorSchemePreference
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.food.ShowBrandedFoodPreference
import com.faltenreich.diaguard.preference.food.ShowCommonFoodPreference
import com.faltenreich.diaguard.preference.food.ShowCustomFoodPreference
import com.faltenreich.diaguard.preference.screen.StartScreenPreference
import com.faltenreich.diaguard.preference.store.SetPreferenceUseCase
import com.faltenreich.diaguard.shared.logging.Logger

class ImportLegacyPreferencesUseCase(
    private val legacyRepository: LegacyRepository,
    private val setPreference: SetPreferenceUseCase,
) {

    suspend operator fun invoke() {
        Logger.debug("Importing legacy preferences")
        importPreference(ColorSchemePreference)
        importPreference(DecimalPlacesPreference)
        importPreference(ShowBrandedFoodPreference)
        importPreference(ShowCommonFoodPreference)
        importPreference(ShowCustomFoodPreference)
        importPreference(StartScreenPreference)
        Logger.debug("Imported legacy preferences")
    }

    private suspend inline fun <reified Store: Any, Domain> importPreference(preference: Preference<Store, Domain>) {
        val import = legacyRepository.getPreference(preference)
        if (import != null) {
            setPreference(preference, import)
            Logger.debug("Imported legacy for $preference: $import")
        } else {
            Logger.debug("Imported nothing for $preference")
        }
    }
}