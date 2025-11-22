package com.faltenreich.diaguard.persistence.sqldelight

import app.cash.sqldelight.Transacter
import com.faltenreich.diaguard.injection.inject

interface SqlDelightDao<SqlDelightQueries: Transacter> {

    val queries: SqlDelightQueries
        get() = getQueries(
            SqlDelightApi(inject<SqlDelightDriverFactory>().createDriver(SqlDelightApi.Schema))
        )

    fun getQueries(api: SqlDelightApi): SqlDelightQueries

    fun transaction(transact: () -> Unit) {
        queries.transaction { transact() }
    }
}