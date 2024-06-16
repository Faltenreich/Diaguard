package com.faltenreich.diaguard.backup.legacy.query

import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDateTimeFactory
import com.faltenreich.diaguard.shared.database.sqlite.SqliteDatabase
import com.faltenreich.diaguard.shared.test.FileFactory
import com.faltenreich.diaguard.tag.Tag
import org.junit.Assert
import org.junit.Test

class TagLegacyQueriesTest {

    private val database = SqliteDatabase(file = FileFactory.createFromAssets("diaguard.db"))
    private val dateTimeFactory = KotlinxDateTimeFactory()

    private val queries = TagLegacyQueries(
        database = database,
        dateTimeFactory = dateTimeFactory,
    )

    @Test
    fun readsTags() {
        val expected = arrayOf(
            Tag.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865197888),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865197888),
                name = "after the sport",
            ),
            Tag.Legacy(
                id = 2,
                createdAt = dateTimeFactory.dateTime(millis = 1717865197898),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865197898),
                name = "before the sport",
            ),
            Tag.Legacy(
                id = 3,
                createdAt = dateTimeFactory.dateTime(millis = 1717865197898),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865197898),
                name = "sick",
            ),
            Tag.Legacy(
                id = 4,
                createdAt = dateTimeFactory.dateTime(millis = 1717865197899),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865197899),
                name = "tired",
            ),
            Tag.Legacy(
                id = 5,
                createdAt = dateTimeFactory.dateTime(millis = 1717865197899),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865197899),
                name = "excited",
            ),
            Tag.Legacy(
                id = 6,
                createdAt = dateTimeFactory.dateTime(millis = 1717865197899),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865197899),
                name = "happy",
            ),
            Tag.Legacy(
                id = 7,
                createdAt = dateTimeFactory.dateTime(millis = 1717865197899),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865197899),
                name = "falling asleep",
            ),
            Tag.Legacy(
                id = 8,
                createdAt = dateTimeFactory.dateTime(millis = 1717865197899),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865197899),
                name = "just woke up",
            ),
        )
        val actual = queries.getAll().toTypedArray()
        Assert.assertArrayEquals(expected, actual)
    }
}