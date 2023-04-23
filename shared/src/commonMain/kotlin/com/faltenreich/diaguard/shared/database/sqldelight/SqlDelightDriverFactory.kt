package com.faltenreich.diaguard.shared.database.sqldelight

import app.cash.sqldelight.db.SqlDriver
import com.faltenreich.diaguard.shared.architecture.Context

expect class SqlDelightDriverFactory constructor(context: Context) {

    fun createDriver(): SqlDriver
}