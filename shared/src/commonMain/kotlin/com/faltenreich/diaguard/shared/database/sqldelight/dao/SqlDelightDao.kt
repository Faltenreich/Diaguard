package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.Transacter
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightDatabase
import com.faltenreich.diaguard.shared.di.inject

interface SqlDelightDao<SqlDelightQueries: Transacter> {

    val queries: SqlDelightQueries
        get() = getQueries(inject<SqlDelightDatabase>().api)

    fun getQueries(api: SqlDelightApi): SqlDelightQueries

    fun transaction(transact: () -> Unit) {
        queries.transaction { transact() }
    }
}