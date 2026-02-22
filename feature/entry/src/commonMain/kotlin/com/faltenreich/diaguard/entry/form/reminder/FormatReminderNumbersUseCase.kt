package com.faltenreich.diaguard.entry.form.reminder

import com.faltenreich.diaguard.localization.NumberFormatter
import kotlin.time.Duration

class FormatReminderNumbersUseCase(
    private val numberFormatter: NumberFormatter,
) {

    operator fun invoke(duration: Duration): List<Int> {
        return duration.toComponents { hours, minutes, seconds, _ ->
            val string = "${format(hours)}${format(minutes)}${format(seconds)}"
            string.toCharArray().map(Char::digitToInt)
        }
    }

    private fun format(number: Number): String {
        return numberFormatter.invoke(number.toInt(), width = 2, padZeroes = true)
    }
}