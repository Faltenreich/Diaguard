package com.faltenreich.diaguard.backup.seed

import androidx.compose.ui.text.intl.Locale

class SeedMapper {

    operator fun invoke(seed: SeedLocalization): String {
        val locale = Locale.current
        return when (locale.language) {
            else -> seed.en
        }
    }
}