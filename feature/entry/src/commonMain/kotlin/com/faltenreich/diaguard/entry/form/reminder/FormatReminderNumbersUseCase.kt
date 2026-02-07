package com.faltenreich.diaguard.entry.form.reminder

import com.faltenreich.diaguard.localization.NumberFormatter
import kotlin.time.Duration

class FormatReminderNumbersUseCase(
    private val numberFormatter: NumberFormatter,
) {

    operator fun invoke(duration: Duration): List<Int> {
        return duration.toComponents { hours, minutes, seconds, _ ->
            // TODO: Format via UseCase
            val format =
                { number: Int -> numberFormatter.invoke(number, width = 2, padZeroes = true) }
            val string = "${format(hours.toInt())}${format(minutes)}${format(seconds)}"
            string.toCharArray().map(Char::digitToInt)
        }
    }
}