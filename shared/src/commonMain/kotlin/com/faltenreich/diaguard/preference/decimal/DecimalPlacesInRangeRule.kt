package com.faltenreich.diaguard.preference.decimal

import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.validation.ValidationRule
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.decimal_places_error

class DecimalPlacesInRangeRule(
    private val localization: Localization = inject()
) : ValidationRule<Int> {

    override fun check(input: Int): ValidationResult<Int> {
        return when {
            input < range.first -> ValidationResult.Failure(input, localizedError())
            input > range.last -> ValidationResult.Failure(input, localizedError())
            else -> ValidationResult.Success(input)
        }
    }

    private fun localizedError(): String {
        return localization.getString(
            Res.string.decimal_places_error,
            range.first,
            range.last,
        )
    }

    companion object {

        private val range = 0 .. 3
    }
}