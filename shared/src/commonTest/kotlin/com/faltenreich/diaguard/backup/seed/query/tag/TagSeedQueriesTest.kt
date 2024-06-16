package com.faltenreich.diaguard.backup.seed.query.tag

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.tag.Tag
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals

class TagSeedQueriesTest : TestSuite {

    private val queries: TagSeedQueries by inject()

    @Test
    fun `imports seed`() {
        val expected = listOf(
            Tag.Seed(name = "just woke up"),
            Tag.Seed(name = "falling asleep"),
            Tag.Seed(name = "happy"),
            Tag.Seed(name = "excited"),
            Tag.Seed(name = "tired"),
            Tag.Seed(name = "sick"),
            Tag.Seed(name = "before the sport"),
            Tag.Seed(name = "after the sport"),
        )
        val actual = queries.getAll()
        assertEquals(expected, actual)
    }
}