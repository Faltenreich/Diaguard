package com.faltenreich.diaguard.test

import kotlin.test.assertEquals

fun <T> assertContentEquals(
    expected: Collection<T>,
    actual: Collection<T>,
    ignoreOrder: Boolean = true,
) {
    if (ignoreOrder) {
        assertEquals(expected.toSet(), actual.toSet())
    } else {
        assertEquals(expected, actual)
    }
}