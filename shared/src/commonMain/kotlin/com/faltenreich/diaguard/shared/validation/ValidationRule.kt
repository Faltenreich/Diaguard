package com.faltenreich.diaguard.shared.validation

interface ValidationRule<Input> {

    fun check(input: Input): ValidationResult<Input>
}