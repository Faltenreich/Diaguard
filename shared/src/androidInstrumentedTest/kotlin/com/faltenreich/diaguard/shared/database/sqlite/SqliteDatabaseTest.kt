package com.faltenreich.diaguard.shared.database.sqlite

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faltenreich.diaguard.shared.test.FileFactory
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SqliteDatabaseTest {

    @Test
    fun readsData() {
        val file = FileFactory.createFromAssets("diaguard.db")
        val database = SqliteDatabase(file)

        val ids = mutableListOf<Long?>()
        database.query("entry") {
            ids.add(getLong("_id"))
        }

        Assert.assertTrue(ids.filterNotNull().isNotEmpty())
    }
}