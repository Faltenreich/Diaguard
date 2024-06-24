package com.faltenreich.diaguard.preference.decimal

import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.decimal_places_illustration

class IllustrateDecimalPlacesUseCase(
    private val localization: Localization,
    private val numberFormatter: NumberFormatter,
) {

    operator fun invoke(decimalPlaces: Int): String {
        return localization.getString(
            Res.string.decimal_places_illustration,
            numberFormatter(EXAMPLE_NUMBER, scale = EXAMPLE_SCALE),
            numberFormatter(EXAMPLE_NUMBER, scale = decimalPlaces),
        )
    }

    companion object {

        private const val EXAMPLE_SCALE = 3
        private const val EXAMPLE_NUMBER = .123
    }
}