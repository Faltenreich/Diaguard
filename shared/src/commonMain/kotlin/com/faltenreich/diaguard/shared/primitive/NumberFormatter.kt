package com.faltenreich.diaguard.shared.primitive

import androidx.compose.ui.text.intl.Locale

expect class NumberFormatter constructor() {

    operator fun invoke(
        number: Double,
        scale: Int,
        locale: Locale,
    ): String
}