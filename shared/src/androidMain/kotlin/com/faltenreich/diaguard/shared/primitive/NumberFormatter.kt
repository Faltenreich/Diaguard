package com.faltenreich.diaguard.shared.primitive

import java.text.NumberFormat

actual class NumberFormatter {

    actual fun format(number: Double): String {
        return NumberFormat.getInstance().format(number)
    }
}