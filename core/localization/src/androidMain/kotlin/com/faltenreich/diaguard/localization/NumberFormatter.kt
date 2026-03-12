package com.faltenreich.diaguard.localization

import com.faltenreich.diaguard.injection.inject
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

    actual operator fun invoke(
        number: Int,
        width: Int,
        padZeroes: Boolean,
    ): String {
        return "%${if (padZeroes) "0" else ""}${width}d".format(number)
    }
}