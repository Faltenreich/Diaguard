package com.faltenreich.diaguard.shared.test

import io.mockative.ResultBuilder

infix fun <R> ResultBuilder<R>.invokes(block: () -> R) {
    return invokes(block)
}

infix fun <R> ResultBuilder<R>.returns(value: R) {
    returns(value)
}

infix fun <R> ResultBuilder<R>.throws(throwable: Throwable) {
    throws(throwable)
}