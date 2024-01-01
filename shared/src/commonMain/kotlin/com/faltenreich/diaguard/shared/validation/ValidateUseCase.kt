package com.faltenreich.diaguard.shared.validation

class ValidateUseCase {

    operator fun <Input> invoke(
        input: Input,
        vararg rules: Rule<Input>,
    ): Result<Unit> {
        for (rule in rules) {
            val result = rule.check(input)
            if (result.isFailure) {
                return result
            }
        }
        return Result.success(Unit)
    }
}