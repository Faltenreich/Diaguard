package com.faltenreich.diaguard.architecture.either

interface ValidationRule<Input> {

    fun check(input: Input): ValidationResult<Input>
}