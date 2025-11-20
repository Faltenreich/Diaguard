package com.faltenreich.diaguard.persistence.sqldelight

import app.cash.sqldelight.db.SqlDriver

fun interface SqlDelightDriverFactory {

    fun createDriver(): SqlDriver
}