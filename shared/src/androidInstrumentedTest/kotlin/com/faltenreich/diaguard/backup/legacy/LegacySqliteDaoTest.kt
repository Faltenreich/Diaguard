package com.faltenreich.diaguard.backup.legacy

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDateTimeFactory
import com.faltenreich.diaguard.shared.database.sqlite.SqliteDatabase
import com.faltenreich.diaguard.shared.test.FileFactory
import com.faltenreich.diaguard.tag.Tag
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LegacySqliteDaoTest {

    private val database = SqliteDatabase(file = FileFactory.createFromAssets("diaguard.db"))
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
        val expected = arrayOf(
            Tag.Legacy(
                createdAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.888"),
                updatedAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.888"),
                name = "after the sport",
            ),
            Tag.Legacy(
                createdAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.898"),
                updatedAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.898"),
                name = "before the sport",
            ),
            Tag.Legacy(
                createdAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.898"),
                updatedAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.898"),
                name = "sick",
            ),
            Tag.Legacy(
                createdAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.899"),
                updatedAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.899"),
                name = "tired",
            ),
            Tag.Legacy(
                createdAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.899"),
                updatedAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.899"),
                name = "excited",
            ),
            Tag.Legacy(
                createdAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.899"),
                updatedAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.899"),
                name = "happy",
            ),
            Tag.Legacy(
                createdAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.899"),
                updatedAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.899"),
                name = "falling asleep",
            ),
            Tag.Legacy(
                createdAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.899"),
                updatedAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.899"),
                name = "just woke up",
            ),
        )
        val actual = dao.getTags().toTypedArray()
        Assert.assertArrayEquals(
            expected,
            actual,
        )
    }
}