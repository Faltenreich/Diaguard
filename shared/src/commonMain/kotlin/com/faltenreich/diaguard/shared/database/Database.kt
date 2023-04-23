package com.faltenreich.diaguard.shared.database

import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightDatabase
import com.faltenreich.diaguard.shared.di.inject
import org.koin.core.annotation.Single

@Single
class Database {

    val sqlDelightDatabase: SqlDelightDatabase = SqlDelightDatabase()

    init {
        sqlDelightDatabase.createDatabase(inject())
    }
}