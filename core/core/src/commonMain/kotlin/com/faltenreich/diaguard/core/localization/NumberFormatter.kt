package com.faltenreich.diaguard.core.localization

expect class NumberFormatter constructor() {

    operator fun invoke(
        number: Double,
        scale: Int,
    ): String

    operator fun invoke(
        number: Int,
        width: Int,
        padZeroes: Boolean,
    ): String
}