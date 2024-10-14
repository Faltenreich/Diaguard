package com.faltenreich.diaguard.shared.primitive

import androidx.compose.ui.text.intl.Locale
import java.text.NumberFormat

actual class NumberFormatter {

    actual operator fun invoke(
        number: Double,
        scale: Int,
        locale: Locale,
    ): String {
        val format = NumberFormat.getInstance(locale.platformLocale)
        format.maximumFractionDigits = scale
        return format.format(number)
    }
}