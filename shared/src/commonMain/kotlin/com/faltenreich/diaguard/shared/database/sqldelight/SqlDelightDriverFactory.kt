package com.faltenreich.diaguard.shared.database.sqldelight

import app.cash.sqldelight.db.SqlDriver

fun interface SqlDelightDriverFactory {

    fun createDriver(): SqlDriver
}