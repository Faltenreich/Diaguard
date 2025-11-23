package com.faltenreich.diaguard.data.sqldelight

private const val LONG_TRUE = 1L
private const val LONG_FALSE = 0L

internal fun Long.toSqlLiteBoolean(): Boolean {
    return this == LONG_TRUE
}

internal fun Boolean.toSqlLiteLong(): Long {
    return if (this) LONG_TRUE else LONG_FALSE
}