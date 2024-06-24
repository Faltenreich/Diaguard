package com.faltenreich.diaguard.shared.database.sqldelight

import app.cash.sqldelight.db.SqlDriver

expect class SqlDelightDriverFactory {

    fun createDriver(): SqlDriver
}