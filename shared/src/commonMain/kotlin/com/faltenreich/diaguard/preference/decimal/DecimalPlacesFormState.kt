package com.faltenreich.diaguard.preference.decimal

data class DecimalPlacesFormState(
    val decimalPlaces: Int,
    val range: IntRange,
    val illustration: String,
)