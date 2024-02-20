package com.faltenreich.diaguard.shared.primitive

expect class NumberFormatter {

    operator fun invoke(number: Double): String
}