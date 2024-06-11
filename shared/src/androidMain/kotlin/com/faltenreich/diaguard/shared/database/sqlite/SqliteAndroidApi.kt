package com.faltenreich.diaguard.shared.database.sqlite

import android.database.sqlite.SQLiteDatabase
import com.faltenreich.diaguard.shared.logging.Logger
import java.io.File

class SqliteAndroidApi(file: File) : SqliteApi {

    private val database: SQLiteDatabase? by lazy {
        if (file.exists()) SQLiteDatabase.openDatabase(file.absolutePath, null, 0)
        else null
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