package com.faltenreich.diaguard.shared.primitive

import java.text.NumberFormat

actual class NumberFormatter {

    actual operator fun invoke(number: Double): String {
        return NumberFormat.getInstance().format(number)
    }
}