package com.faltenreich.diaguard.preference.decimalplaces

import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.localization.NumberFormatter
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.decimal_places_illustration

internal class IllustrateDecimalPlacesUseCase(
    private val numberFormatter: NumberFormatter,
    private val localization: Localization,
) {

    operator fun invoke(decimalPlaces: Int): String {
        return localization.getString(
            Res.string.decimal_places_illustration,
            numberFormatter(
                number = EXAMPLE_NUMBER,
                scale = EXAMPLE_SCALE,
            ),
            numberFormatter(
                number = EXAMPLE_NUMBER,
                scale = decimalPlaces,
            ),
        )
    }

    companion object {

        private const val EXAMPLE_SCALE = 4
        private const val EXAMPLE_NUMBER = .1234
    }
}