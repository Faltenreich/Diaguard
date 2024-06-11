package com.faltenreich.diaguard.shared.database.sqlite

interface SqliteApi {

    fun queryEach(table: String, onEach: SqliteRow.() -> Unit)
}