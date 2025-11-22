package com.faltenreich.diaguard.persistence.sqldelight

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

internal class SqlDelightInMemoryDriverFactory : SqlDelightDriverFactory {

    override fun createDriver(schema: SqlSchema<QueryResult.Value<Unit>>): SqlDriver {
        return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
            .apply { schema.create(this) }
    }
}