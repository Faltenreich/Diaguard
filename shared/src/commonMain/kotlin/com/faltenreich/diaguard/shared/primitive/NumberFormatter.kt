package com.faltenreich.diaguard.shared.primitive

expect class NumberFormatter constructor() {

    operator fun invoke(number: Double): String
}