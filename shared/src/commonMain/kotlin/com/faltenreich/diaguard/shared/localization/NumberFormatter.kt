package com.faltenreich.diaguard.shared.localization

expect class NumberFormatter constructor() {

    operator fun invoke(
        number: Double,
        scale: Int,
    ): String
}