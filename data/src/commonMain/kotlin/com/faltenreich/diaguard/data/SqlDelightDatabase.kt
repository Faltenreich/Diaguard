package com.faltenreich.diaguard.data

import com.faltenreich.diaguard.persistence.sqldelight.SqlDelightDriverFactory

class SqlDelightDatabase(driverFactory: SqlDelightDriverFactory) {

    val api = SqlDelightApi(driverFactory.createDriver(SqlDelightApi.Schema))
}