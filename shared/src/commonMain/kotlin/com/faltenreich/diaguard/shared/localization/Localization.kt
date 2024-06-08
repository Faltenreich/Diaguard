package com.faltenreich.diaguard.shared.localization

import androidx.compose.ui.text.intl.Locale

class Localization(
    private val resourceLocalization: ResourceLocalization,
) : ResourceLocalization by resourceLocalization {

    fun getLocale(): Locale {
        // FIXME: Not testable
        return Locale.current
    }
}