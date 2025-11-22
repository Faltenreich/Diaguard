package com.faltenreich.diaguard.persistence.sqldelight

class SqlDelightDatabase(driverFactory: SqlDelightDriverFactory) {

    val api = SqlDelightApi(driverFactory.createDriver(SqlDelightApi.Schema))
}