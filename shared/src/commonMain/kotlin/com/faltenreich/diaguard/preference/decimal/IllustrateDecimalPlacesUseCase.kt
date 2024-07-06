package com.faltenreich.diaguard.preference.decimal

import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.decimal_places_illustration

class IllustrateDecimalPlacesUseCase(
    private val numberFormatter: NumberFormatter,
    private val localization: Localization,
) {

    operator fun invoke(decimalPlaces: Int): String {
        return localization.getString(
            Res.string.decimal_places_illustration,
            numberFormatter(
                number = EXAMPLE_NUMBER,
                scale = EXAMPLE_SCALE,
                locale = localization.getLocale(),
            ),
            numberFormatter(
                number = EXAMPLE_NUMBER,
                scale = decimalPlaces,
                locale = localization.getLocale(),
            ),
        )
    }

    companion object {

        private const val EXAMPLE_SCALE = 4
        private const val EXAMPLE_NUMBER = .1234
    }
}