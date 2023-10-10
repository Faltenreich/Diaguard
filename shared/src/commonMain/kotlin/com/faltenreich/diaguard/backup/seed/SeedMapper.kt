package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.shared.localization.Localization

class SeedMapper(private val localization: Localization) {

    operator fun invoke(seed: SeedLocalization): String {
        val locale = localization.currentLocale
        when (locale) {}
        TODO()
    }
}