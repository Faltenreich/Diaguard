package com.faltenreich.diaguard.shared.database.sqldelight

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class SqlDelightDriverFactory(private val context: Context) {

    actual fun createDriver(): SqlDriver {
        // Ships driver for native SQLite database bundled with Android
        // https://developer.android.com/reference/android/database/sqlite/package-summary.html
        return AndroidSqliteDriver(SqlDelightApi.Schema, context, DATABASE_FILE_NAME)
    }

    companion object {

        private const val DATABASE_FILE_NAME = "diaguard.sqldelight.db"
    }
}