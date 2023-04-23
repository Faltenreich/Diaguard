package com.faltenreich.diaguard.shared.database.sqldelight

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.faltenreich.diaguard.Database

actual class SqlDelightDriverFactory(private val context: Context) {

    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(Database.Schema, context, DATABASE_FILE_NAME)
    }

    companion object {

        private const val DATABASE_FILE_NAME = "diaguard.sqldelight.db"
    }
}