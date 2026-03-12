package com.faltenreich.diaguard.data.preference.decimal

import com.faltenreich.diaguard.data.preference.Preference

data object DecimalPlacesPreference : Preference<Int, Int> {

    override val key = "Decimal places"

    override val default = 1

    override val onRead = { decimalPlaces: Int -> decimalPlaces }

    override val onWrite = { decimalPlaces: Int -> decimalPlaces }
}