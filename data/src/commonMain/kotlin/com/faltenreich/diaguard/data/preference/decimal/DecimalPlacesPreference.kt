package com.faltenreich.diaguard.data.preference.decimal

import com.faltenreich.diaguard.data.preference.Preference
import diaguard.data.generated.resources.Res
import diaguard.data.generated.resources.preference_decimal_places

data object DecimalPlacesPreference : Preference<Int, Int> {

    override val key = Res.string.preference_decimal_places

    override val default = 1

    override val onRead = { decimalPlaces: Int -> decimalPlaces }

    override val onWrite = { decimalPlaces: Int -> decimalPlaces }
}