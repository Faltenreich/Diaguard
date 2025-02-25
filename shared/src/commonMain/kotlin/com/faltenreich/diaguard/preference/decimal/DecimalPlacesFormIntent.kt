package com.faltenreich.diaguard.preference.decimal

sealed interface DecimalPlacesFormIntent {

    data class Update(val decimalPlaces: Int) : DecimalPlacesFormIntent
}