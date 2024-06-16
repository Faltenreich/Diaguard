package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.TestSuite
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertTrue

class LegacyRepositoryTest : TestSuite {

    private val repository: LegacyRepository by inject()

    @Test
    fun `returns entries`() {
        assertTrue(repository.getEntries().isNotEmpty())
    }

    @Test
    fun `returns values`() {
        assertTrue(repository.getMeasurementValues().isNotEmpty())
    }

    @Test
    fun `returns food`() {
        assertTrue(repository.getFood().isNotEmpty())
    }

    @Test
    fun `returns food eaten`() {
        assertTrue(repository.getFoodEaten().isNotEmpty())
    }

    @Test
    fun `returns tags`() {
        assertTrue(repository.getTags().isNotEmpty())
    }

    @Test
    fun `returns entry tags`() {
        assertTrue(repository.getEntryTags().isNotEmpty())
    }
}