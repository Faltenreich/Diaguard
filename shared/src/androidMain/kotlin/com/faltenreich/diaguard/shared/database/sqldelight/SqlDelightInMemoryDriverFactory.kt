package com.faltenreich.diaguard.shared.database.sqldelight

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

class SqlDelightInMemoryDriverFactory : SqlDelightDriverFactory {

    override fun createDriver(): SqlDriver {
        return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
            SqlDelightApi.Schema.create(this)
        }
    }
}