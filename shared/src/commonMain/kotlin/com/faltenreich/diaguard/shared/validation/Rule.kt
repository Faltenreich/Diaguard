package com.faltenreich.diaguard.shared.validation

interface Rule<Input> {

    fun check(input: Input): Result<Unit>
}