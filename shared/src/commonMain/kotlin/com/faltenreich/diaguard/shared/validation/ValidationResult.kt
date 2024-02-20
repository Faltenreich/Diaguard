package com.faltenreich.diaguard.shared.validation

sealed class ValidationResult<T>(val data: T) {

    class Success<T>(data: T) : ValidationResult<T>(data)

    class Failure<T>(data: T, val error: String) : ValidationResult<T>(data)
}