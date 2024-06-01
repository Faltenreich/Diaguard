package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.TestSuite
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertNotNull

class LegacyRepositoryTest : TestSuite {

    private val repository: LegacyRepository by inject()

    @Test
    fun `returns entries`() {
        assertNotNull(repository.getEntries())
    }

    @Test
    fun `returns values`() {
        assertNotNull(repository.getMeasurementValues())
    }

    @Test
    fun `returns tags`() {
        assertNotNull(repository.getTags())
    }
}