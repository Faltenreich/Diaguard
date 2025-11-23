package com.faltenreich.diaguard.persistence.database

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

internal class SqlDelightDiskDriverFactory(private val context: Context) : SqlDelightDriverFactory {

    override fun createDriver(schema: SqlSchema<QueryResult.Value<Unit>>): SqlDriver {
        // SQLite is bundled with operating system and its version aligns with minSdk
        // https://developer.android.com/reference/android/database/sqlite/package-summary.html
        return AndroidSqliteDriver(
            schema = schema,
            context = context,
            name = DATABASE_FILE_NAME,
            callback = object : AndroidSqliteDriver.Callback(schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    db.setForeignKeyConstraintsEnabled(true)
                }
            }
        )
    }

    companion object {

        private const val DATABASE_FILE_NAME = "diaguard.sqldelight.db"
    }
}