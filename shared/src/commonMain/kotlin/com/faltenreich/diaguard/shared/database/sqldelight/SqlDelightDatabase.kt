package com.faltenreich.diaguard.shared.database.sqldelight

import com.faltenreich.diaguard.Database

class SqlDelightDatabase {

    private lateinit var database: Database

    fun createDatabase(driverFactory: SqlDelightDriverFactory): Database {
        val driver = driverFactory.createDriver()
        database = Database(driver)
        return database
    }
}