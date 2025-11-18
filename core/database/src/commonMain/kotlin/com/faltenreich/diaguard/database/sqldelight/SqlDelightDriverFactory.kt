package com.faltenreich.diaguard.database.sqldelight

import app.cash.sqldelight.db.SqlDriver

fun interface SqlDelightDriverFactory {

    fun createDriver(): SqlDriver
}