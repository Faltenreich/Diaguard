package com.faltenreich.diaguard.data

import app.cash.sqldelight.Transacter
import com.faltenreich.diaguard.injection.inject

interface SqlDelightDao<SqlDelightQueries: Transacter> {

    val queries: SqlDelightQueries
        // TODO: Remove hard coupled injection while keeping singleton SqlDelightApi
        get() = getQueries(inject<SqlDelightDatabase>().api)

    fun getQueries(api: SqlDelightApi): SqlDelightQueries

    fun transaction(transact: () -> Unit) {
        queries.transaction { transact() }
    }
}