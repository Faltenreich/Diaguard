package com.faltenreich.diaguard.startup.seed.query.tag

import com.faltenreich.diaguard.data.seed.query.tag.TagSeedQueries
import com.faltenreich.diaguard.startup.startupModule
import com.faltenreich.diaguard.test.TestSuite
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertTrue

class TagSeedQueriesTest : TestSuite(startupModule()) {

    private val queries: TagSeedQueries by inject()

    @Test
    fun `imports seed`() {
        assertTrue(queries.getAll().isNotEmpty())
    }
}