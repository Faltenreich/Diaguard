package com.faltenreich.diaguard.database.sqldelight

class SqlDelightDatabase(driverFactory: SqlDelightDriverFactory) {

    val api = SqlDelightApi(driverFactory.createDriver())
}