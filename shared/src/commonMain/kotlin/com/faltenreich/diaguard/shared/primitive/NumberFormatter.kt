package com.faltenreich.diaguard.shared.primitive

import androidx.compose.ui.text.intl.Locale
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization

expect class NumberFormatter constructor() {

    operator fun invoke(
        number: Double,
        scale: Int = 2,
        // TODO: Extract
        locale: Locale = inject<Localization>().getLocale(),
    ): String
}