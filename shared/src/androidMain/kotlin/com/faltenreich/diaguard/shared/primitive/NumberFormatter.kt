package com.faltenreich.diaguard.shared.primitive

import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import java.text.NumberFormat

actual class NumberFormatter(private val localization: Localization) {

    actual constructor() : this(localization = inject())

    actual operator fun invoke(
        number: Double,
        scale: Int,
    ): String {
        val locale = localization.getLocale()
        val format = NumberFormat.getInstance(locale.platformLocale)
        format.maximumFractionDigits = scale
        return format.format(number)
    }
}