package com.faltenreich.diaguard.data.legacy.query

import androidx.test.platform.app.InstrumentationRegistry
import com.faltenreich.diaguard.data.FileFactory
import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.datetime.DateTimeAndroidApi
import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDateTimeFactory
import com.faltenreich.diaguard.localization.ComposeLocalization
import com.faltenreich.diaguard.persistence.database.SqliteDatabase
import org.junit.Assert
import org.junit.Test

class EntryLegacyQueriesTest {

    private val database = SqliteDatabase(file = FileFactory.createFromAssets("diaguard.db"))
    private val dateTimePlatformApi = DateTimeAndroidApi(
        localization = ComposeLocalization(),
        context = InstrumentationRegistry.getInstrumentation().context,
    )
    private val dateTimeFactory = KotlinxDateTimeFactory(dateTimePlatformApi)

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