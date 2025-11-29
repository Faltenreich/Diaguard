package com.faltenreich.diaguard.shared.result

sealed interface Result<out Data, out Error> {

    data class Success<out Data>(val value: Data) : Result<Data, Nothing>

    data class Failure<out Error>(val value: Error) : Result<Nothing, Error>
}