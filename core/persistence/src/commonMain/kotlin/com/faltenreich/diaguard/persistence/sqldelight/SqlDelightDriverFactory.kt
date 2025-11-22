package com.faltenreich.diaguard.persistence.sqldelight

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema

fun interface SqlDelightDriverFactory {

    fun createDriver(schema: SqlSchema<QueryResult.Value<Unit>>): SqlDriver
}