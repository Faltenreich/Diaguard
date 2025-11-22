package com.faltenreich.diaguard.persistence.sqldelight

import app.cash.sqldelight.Transacter
import com.faltenreich.diaguard.injection.inject

interface SqlDelightDao<SqlDelightQueries: Transacter> {

    val queries: SqlDelightQueries
        get() = getQueries(inject<SqlDelightDatabase>().api)

    fun getQueries(api: SqlDelightApi): SqlDelightQueries

    fun transaction(transact: () -> Unit) {
        queries.transaction { transact() }
    }
}