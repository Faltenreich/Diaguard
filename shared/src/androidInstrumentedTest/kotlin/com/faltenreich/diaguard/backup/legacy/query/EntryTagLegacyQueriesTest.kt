package com.faltenreich.diaguard.backup.legacy.query

import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDateTimeFactory
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.shared.database.sqlite.SqliteDatabase
import com.faltenreich.diaguard.shared.test.FileFactory
import org.junit.Assert
import org.junit.Test

class EntryTagLegacyQueriesTest {

    private val database = SqliteDatabase(file = FileFactory.createFromAssets("diaguard.db"))
    private val dateTimeFactory = KotlinxDateTimeFactory()

    private val queries = EntryTagLegacyQueries(
        database = database,
        dateTimeFactory = dateTimeFactory,
    )

    @Test
    fun readsEntryTags() {
        val expected = arrayOf(
            EntryTag.Legacy(
                createdAt = dateTimeFactory.dateTime(millis = 1717865198200),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198200),
                entryId = 1,
                tagId = 1,
            )
        )
        val actual = queries.getAll().toTypedArray()
        Assert.assertArrayEquals(expected, actual)
    }
}