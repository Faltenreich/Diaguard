package com.faltenreich.diaguard.shared.database.sqldelight

class SqlDelightDatabase {

    lateinit var database: SqlDelightGeneratedDatabase

    fun createDatabase(driverFactory: SqlDelightDriverFactory): SqlDelightGeneratedDatabase {
        val driver = driverFactory.createDriver()
        database = SqlDelightGeneratedDatabase(driver)
        return database
    }
}