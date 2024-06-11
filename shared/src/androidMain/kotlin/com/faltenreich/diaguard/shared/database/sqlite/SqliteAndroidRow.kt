package com.faltenreich.diaguard.shared.database.sqlite

import android.database.Cursor
import com.faltenreich.diaguard.shared.logging.Logger

class SqliteAndroidRow(private val cursor: Cursor) : SqliteRow {

    override fun getLong(column: String): Long? {
        return try {
            cursor.getLong(cursor.getColumnIndexOrThrow(column))
        } catch (exception: Exception) {
            Logger.error("Failed to getLong for column $column")
            null
        }
    }

    override fun getDouble(column: String): Double? {
        return try {
            return cursor.getDouble(cursor.getColumnIndexOrThrow(column))
        } catch (exception: Exception) {
            Logger.error("Failed to getDouble for column $column")
            null
        }
    }

    override fun getString(column: String): String? {
        return try {
            return cursor.getString(cursor.getColumnIndexOrThrow(column))
        } catch (exception: Exception) {
            Logger.error("Failed to getString for column $column")
            null
        }
    }
}