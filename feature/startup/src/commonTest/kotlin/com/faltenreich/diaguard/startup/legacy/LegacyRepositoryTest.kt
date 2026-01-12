package com.faltenreich.diaguard.startup.legacy

import com.faltenreich.diaguard.data.legacy.LegacyRepository
import com.faltenreich.diaguard.startup.startupModule
import com.faltenreich.diaguard.test.TestSuite
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertTrue

class LegacyRepositoryTest : TestSuite(startupModule()) {

    private val repository: LegacyRepository by inject()

    @Test
    fun `returns entries`() = runTest {
        assertTrue(repository.getEntries().isNotEmpty())
    }

    @Test
    fun `returns values`() = runTest {
        assertTrue(repository.getMeasurementValues().isNotEmpty())
    }

    @Test
    fun `returns food`() = runTest {
        assertTrue(repository.getFood().isNotEmpty())
    }

    @Test
    fun `returns food eaten`() = runTest {
        assertTrue(repository.getFoodEaten().isNotEmpty())
    }

    @Test
    fun `returns tags`() = runTest {
        assertTrue(repository.getTags().isNotEmpty())
    }

    @Test
    fun `returns entry tags`() = runTest {
        assertTrue(repository.getEntryTags().isNotEmpty())
    }
}