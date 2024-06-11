package com.faltenreich.diaguard.shared.database.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.faltenreich.diaguard.shared.logging.Logger

class SqliteAndroidApi(context: Context) : SqliteApi {

    private val database: SQLiteDatabase?

    init {
        val databaseFile = context.getDatabasePath("diaguard.db")
        database = if (databaseFile.exists()) {
            SQLiteDatabase.openDatabase(databaseFile.absolutePath, null, 0)
        } else {
            null
        }
    }

    override fun queryEach(
        table: String,
        onEach: SqliteRow.() -> Unit
    ) {
        val database = database ?: return
        with(database.query(table, null, null, null, null, null, null)) {
            while (moveToNext()) {
                try {
                    val row = SqliteAndroidRow(cursor = this)
                    row.onEach()
                } catch (exception: IllegalArgumentException) {
                    Logger.error("Failed to query table $table", exception)
                }
            }
            close()
        }
    }
}