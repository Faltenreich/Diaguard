package com.faltenreich.diaguard.backup.legacy

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDateTimeFactory
import com.faltenreich.diaguard.shared.database.sqlite.SqliteDatabase
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File


@RunWith(AndroidJUnit4::class)
class LegacySqliteDaoTest {

    private val database = SqliteDatabase(file = File("TODO"))
    private val dateTimeFactory = KotlinxDateTimeFactory()
    private val dao = LegacySqliteDao(database, dateTimeFactory)

    @Test
    fun readsEntries() {
        Assert.assertTrue(dao.getEntries().isNotEmpty())
    }

    @Test
    fun readsMeasurements() {
        Assert.assertTrue(dao.getMeasurementValues().isNotEmpty())
    }

    @Test
    fun readsTags() {
        Assert.assertTrue(dao.getTags().isNotEmpty())
    }
}