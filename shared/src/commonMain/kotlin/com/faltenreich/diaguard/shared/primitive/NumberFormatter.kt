package com.faltenreich.diaguard.shared.primitive

expect class NumberFormatter constructor() {

    fun format(number: Double): String
}