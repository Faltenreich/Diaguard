package com.faltenreich.diaguard.shared.database.sqldelight

class SqlDelightDatabase(driverFactory: SqlDelightDriverFactory) {

    val api = SqlDelightApi(driverFactory.createDriver())
}