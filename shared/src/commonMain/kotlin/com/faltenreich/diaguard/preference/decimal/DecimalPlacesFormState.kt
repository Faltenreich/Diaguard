package com.faltenreich.diaguard.preference.decimal

data class DecimalPlacesFormState(
    val decimalPlaces: Int,
    val illustration: String,
    val enableDecreaseButton: Boolean,
    val enableIncreaseButton: Boolean,
)