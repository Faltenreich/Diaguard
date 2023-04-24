package com.faltenreich.diaguard.shared.database.sqldelight

import org.koin.core.annotation.Single

@Single
class SqlDelightDatabase(driverFactory: SqlDelightDriverFactory) {

    val api = SqlDelightApi(driverFactory.createDriver())
}