package com.faltenreich.diaguard.backup.legacy

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDateTimeFactory
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.food.Food
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
    fun readsFood() {
        val expected = arrayOf(
            Food.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:38.076"),
                updatedAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:38.076"),
                uuid = null,
                name = "Onion quiche, baked (with cake dough)",
                brand = null,
                ingredients = "Onion quiche, baked (with cake dough)",
                labels = "Common food",
                carbohydrates = 15.0,
                energy = 175.0,
                fat = 10.0,
                fatSaturated = 5.0,
                fiber = 1.6,
                proteins = 5.0,
                salt = 4.1,
                sodium = 0.4,
                sugar = 5.1,
            ),
            Food.Legacy(
                id = 962,
                createdAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:38.194"),
                updatedAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:38.194"),
                uuid = null,
                name = "Cola",
                brand = "",
                ingredients = "",
                labels = null,
                carbohydrates = 10.0,
                energy = -1.0,
                fat = -1.0,
                fatSaturated = -1.0,
                fiber = -1.0,
                proteins = -1.0,
                salt = -1.0,
                sodium = -1.0,
                sugar = -1.0,
            ),
        )
        val actual = dao.getFood().toTypedArray()
        Assert.assertArrayEquals(expected, actual)
    }

    @Test
    fun readsFoodEaten() {
        Assert.assertTrue(dao.getFoodEaten().isNotEmpty())
    }

    @Test
    fun readsTags() {
        val expected = arrayOf(
            Tag.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.888"),
                updatedAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.888"),
                name = "after the sport",
            ),
            Tag.Legacy(
                id = 2,
                createdAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.898"),
                updatedAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.898"),
                name = "before the sport",
            ),
            Tag.Legacy(
                id = 3,
                createdAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.898"),
                updatedAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.898"),
                name = "sick",
            ),
            Tag.Legacy(
                id = 4,
                createdAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.899"),
                updatedAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.899"),
                name = "tired",
            ),
            Tag.Legacy(
                id = 5,
                createdAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.899"),
                updatedAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.899"),
                name = "excited",
            ),
            Tag.Legacy(
                id = 6,
                createdAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.899"),
                updatedAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.899"),
                name = "happy",
            ),
            Tag.Legacy(
                id = 7,
                createdAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.899"),
                updatedAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.899"),
                name = "falling asleep",
            ),
            Tag.Legacy(
                id = 8,
                createdAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.899"),
                updatedAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:37.899"),
                name = "just woke up",
            ),
        )
        val actual = dao.getTags().toTypedArray()
        Assert.assertArrayEquals(expected, actual)
    }

    @Test
    fun readsEntryTags() {
        val expected = arrayOf(
            EntryTag.Legacy(
                createdAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:38.200"),
                updatedAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:38.200"),
                entryId = 1,
                tagId = 1,
            )
        )
        val actual = dao.getEntryTags().toTypedArray()
        Assert.assertArrayEquals(expected, actual)
    }
}