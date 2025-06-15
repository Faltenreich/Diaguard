package com.faltenreich.diaguard.shared.database.sqlite

interface SqliteRow {

    fun getLong(column: String): Long?

    fun getDouble(column: String): Double?

    fun getString(column: String): String?
}