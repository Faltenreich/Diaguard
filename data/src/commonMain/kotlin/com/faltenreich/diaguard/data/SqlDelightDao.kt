package com.faltenreich.diaguard.data

import app.cash.sqldelight.Transacter
import com.faltenreich.diaguard.injection.inject
import com.faltenreich.diaguard.persistence.sqldelight.SqlDelightDriverFactory

interface SqlDelightDao<SqlDelightQueries: Transacter> {

    val queries: SqlDelightQueries
        get() = getQueries(
            SqlDelightApi.Companion(inject<SqlDelightDriverFactory>().createDriver(SqlDelightApi.Schema))
        )

    fun getQueries(api: SqlDelightApi): SqlDelightQueries

    fun transaction(transact: () -> Unit) {
        queries.transaction { transact() }
    }
}