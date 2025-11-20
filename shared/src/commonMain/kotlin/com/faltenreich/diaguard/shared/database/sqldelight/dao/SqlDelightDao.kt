package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.Transacter
import com.faltenreich.diaguard.injection.inject
import com.faltenreich.diaguard.persistence.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.persistence.sqldelight.SqlDelightDatabase

interface SqlDelightDao<SqlDelightQueries: Transacter> {

    val queries: SqlDelightQueries
        get() = getQueries(inject<SqlDelightDatabase>().api)

    fun getQueries(api: SqlDelightApi): SqlDelightQueries

    fun transaction(transact: () -> Unit) {
        queries.transaction { transact() }
    }
}