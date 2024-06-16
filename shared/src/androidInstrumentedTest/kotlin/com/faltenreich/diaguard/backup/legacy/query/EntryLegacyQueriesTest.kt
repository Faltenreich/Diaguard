package com.faltenreich.diaguard.backup.legacy.query

import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDateTimeFactory
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.database.sqlite.SqliteDatabase
import com.faltenreich.diaguard.shared.test.FileFactory
import org.junit.Assert
import org.junit.Test

class EntryLegacyQueriesTest {

    private val database = SqliteDatabase(file = FileFactory.createFromAssets("diaguard.db"))
    private val dateTimeFactory = KotlinxDateTimeFactory()

    private val queries = EntryLegacyQueries(
        database = database,
        dateTimeFactory = dateTimeFactory,
    )

    @Test
    fun readsEntries() {
        val expected = arrayOf(
            Entry.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198200),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198200),
                dateTime = dateTimeFactory.dateTime(millis = 1715234400000),
                note = "Hello, World",
            ),
            Entry.Legacy(
                id = 2,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198204),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198204),
                dateTime = dateTimeFactory.dateTime(millis = 1717221600000),
                note = null,
            ),
            Entry.Legacy(
                id = 3,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198208),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198208),
                dateTime = dateTimeFactory.dateTime(millis = 1717308000000),
                note = null,
            ),
        )
        val actual = queries.getAll().toTypedArray()
        Assert.assertArrayEquals(expected, actual)
    }
}