package com.faltenreich.diaguard.preference.decimal

import com.faltenreich.diaguard.preference.Preference
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.preference_decimal_places

data object DecimalPlacesPreference : Preference<Int, Int> {

    override val key = Res.string.preference_decimal_places

    override val default = 1

    override val onRead = { decimalPlaces: Int -> decimalPlaces }

    override val onWrite = { decimalPlaces: Int -> decimalPlaces }
}