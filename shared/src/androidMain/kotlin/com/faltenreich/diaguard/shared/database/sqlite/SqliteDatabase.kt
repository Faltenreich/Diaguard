package com.faltenreich.diaguard.shared.database.sqlite

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.faltenreich.diaguard.shared.logging.Logger
import java.io.File

class SqliteDatabase(file: File) {

    private val database: SQLiteDatabase? = try {
        if (file.exists()) {
            SQLiteDatabase.openDatabase(file.absolutePath, null, 0)
        } else {
            Logger.error("No database found at ${file.absolutePath}")
            null
        }
    } catch (exception: SQLiteException) {
        Logger.error("Failed to open database at ${file.absolutePath}", exception)
        null
    }

    fun query(table: String, onEach: Cursor.() -> Unit) {
        val database = database ?: return
        val query = database.query(table, null, null, null, null, null, null)
        with(query) {
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