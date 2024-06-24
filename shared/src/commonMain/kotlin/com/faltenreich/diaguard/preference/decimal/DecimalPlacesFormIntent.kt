package com.faltenreich.diaguard.preference.decimal

sealed interface DecimalPlacesFormIntent {

    data class SetDecimalPlaces(val decimalPlaces: Int) : DecimalPlacesFormIntent
}