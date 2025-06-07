package com.faltenreich.diaguard.shared.database.sqldelight

object SqlDelightExtensions {

    private const val LONG_TRUE = 1L
    private const val LONG_FALSE = 0L

    fun Long.toSqlLiteBoolean(): Boolean {
        return this == LONG_TRUE
    }

    fun Boolean.toSqlLiteLong(): Long {
        return if (this) LONG_TRUE else LONG_FALSE
    }
}