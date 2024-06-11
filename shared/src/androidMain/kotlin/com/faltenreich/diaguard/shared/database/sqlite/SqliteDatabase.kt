package com.faltenreich.diaguard.shared.database.sqlite

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.faltenreich.diaguard.shared.logging.Logger
import java.io.File

class SqliteDatabase(file: File) {

    private val database = SQLiteDatabase.openDatabase(file.absolutePath, null, 0)

    private fun query(table: String): Cursor {
        return database.query(table, null, null, null, null, null, null)
    }

    fun query(table: String, onEach: Cursor.() -> Unit) {
        with(query(table)) {
            while (moveToNext()) {
                try {
                    onEach()
                } catch (exception: IllegalArgumentException) {
                    Logger.error("Failed to query table $table", exception)
                }
            }
            close()
        }
    }
}

fun Cursor.getLong(column: String): Long? {
    return try {
        getLong(getColumnIndexOrThrow(column))
    } catch (exception: Exception) {
        Logger.error("Failed to getLong for column $column")
        null
    }
}

fun Cursor.getDouble(column: String): Double? {
    return try {
        getDouble(getColumnIndexOrThrow(column))
    } catch (exception: Exception) {
        Logger.error("Failed to getDouble for column $column")
        null
    }
}

fun Cursor.getString(column: String): String? {
    return try {
        getString(getColumnIndexOrThrow(column))
    } catch (exception: Exception) {
        Logger.error("Failed to getString for column $column")
        null
    }
}