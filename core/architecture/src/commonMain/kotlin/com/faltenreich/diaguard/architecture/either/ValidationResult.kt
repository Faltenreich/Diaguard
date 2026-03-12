package com.faltenreich.diaguard.architecture.either

sealed interface ValidationResult<T> {

    data class Success<T>(val data: T) : ValidationResult<T>

    data class Failure<T>(val data: T, val error: String? = null) : ValidationResult<T>
}